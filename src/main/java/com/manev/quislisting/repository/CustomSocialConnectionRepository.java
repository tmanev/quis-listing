package com.manev.quislisting.repository;


import com.manev.quislisting.domain.SocialUserConnection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Custom implementation of {@link ConnectionRepository} in order to support social signin.
 */
public class CustomSocialConnectionRepository implements ConnectionRepository {

    private final String userId;

    private final SocialUserConnectionRepository socialUserConnectionRepository;

    private final ConnectionFactoryLocator connectionFactoryLocator;

    public CustomSocialConnectionRepository(final String userId,
            final SocialUserConnectionRepository socialUserConnectionRepository,
            final ConnectionFactoryLocator connectionFactoryLocator) {
        this.userId = userId;
        this.socialUserConnectionRepository = socialUserConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        final List<SocialUserConnection> socialUserConnections = socialUserConnectionRepository
                .findAllByUserIdOrderByProviderIdAscRankAsc(userId);
        final List<Connection<?>> connections = socialUserConnectionsToConnections(socialUserConnections);
        final MultiValueMap<String, Connection<?>> connectionsByProviderId = new LinkedMultiValueMap<>();
        final Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (final String registeredProviderId : registeredProviderIds) {
            connectionsByProviderId.put(registeredProviderId, Collections.emptyList());
        }
        for (final Connection<?> connection : connections) {
            final String providerId = connection.getKey().getProviderId();
            if (connectionsByProviderId.get(providerId).size() == 0) {
                connectionsByProviderId.put(providerId, new LinkedList<>());
            }
            connectionsByProviderId.add(providerId, connection);
        }
        return connectionsByProviderId;
    }

    @Override
    public List<Connection<?>> findConnections(final String providerId) {
        final List<SocialUserConnection> socialUserConnections = socialUserConnectionRepository
                .findAllByUserIdAndProviderIdOrderByRankAsc(userId, providerId);
        return socialUserConnectionsToConnections(socialUserConnections);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        final List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(
            final MultiValueMap<String, String> providerUserIdsByProviderId) {
        if (providerUserIdsByProviderId == null || providerUserIdsByProviderId.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }

        final MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<>();
        for (final Map.Entry<String, List<String>> entry : providerUserIdsByProviderId.entrySet()) {
            final String providerId = entry.getKey();
            final List<String> providerUserIds = entry.getValue();
            final List<Connection<?>> connections = providerUserIdsToConnections(providerId, providerUserIds);
            connections.forEach(connection -> connectionsForUsers.add(providerId, connection));
        }
        return connectionsForUsers;
    }

    @Override
    public Connection<?> getConnection(final ConnectionKey connectionKey) {
        final SocialUserConnection socialUserConnection = socialUserConnectionRepository
                .findOneByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(),
                        connectionKey.getProviderUserId());
        return Optional.ofNullable(socialUserConnection)
                .map(this::socialUserConnectionToConnection)
                .orElseThrow(() -> new NoSuchConnectionException(connectionKey));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(final Class<A> apiType, final String providerUserId) {
        final String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(final Class<A> apiType) {
        final String providerId = getProviderId(apiType);
        final Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(final Class<A> apiType) {
        final String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    @Override
    @Transactional
    public void addConnection(final Connection<?> connection) {
        final Long rank = getNewMaxRank(connection.getKey().getProviderId()).longValue();
        final SocialUserConnection socialUserConnectionToSave = connectionToUserSocialConnection(connection, rank);
        socialUserConnectionRepository.save(socialUserConnectionToSave);
    }

    @Override
    @Transactional
    public void updateConnection(final Connection<?> connection) {
        final SocialUserConnection socialUserConnection = socialUserConnectionRepository
                .findOneByUserIdAndProviderIdAndProviderUserId(userId, connection.getKey().getProviderId(),
                        connection.getKey().getProviderUserId());
        if (socialUserConnection != null) {
            final SocialUserConnection socialUserConnectionToUpdate = connectionToUserSocialConnection(connection,
                    socialUserConnection.getRank());
            socialUserConnectionToUpdate.setId(socialUserConnection.getId());
            socialUserConnectionRepository.save(socialUserConnectionToUpdate);
        }
    }

    @Override
    @Transactional
    public void removeConnections(final String providerId) {
        socialUserConnectionRepository.deleteByUserIdAndProviderId(userId, providerId);
    }

    @Override
    @Transactional
    public void removeConnection(final ConnectionKey connectionKey) {
        socialUserConnectionRepository.deleteByUserIdAndProviderIdAndProviderUserId(userId,
                connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    private Double getNewMaxRank(final String providerId) {
        final List<SocialUserConnection> socialUserConnections = socialUserConnectionRepository.
                findAllByUserIdAndProviderIdOrderByRankAsc(userId, providerId);
        return socialUserConnections.stream()
                .mapToDouble(SocialUserConnection::getRank)
                .max()
                .orElse(0D) + 1D;
    }

    private Connection<?> findPrimaryConnection(final String providerId) {
        final List<SocialUserConnection> socialUserConnections = socialUserConnectionRepository
                .findAllByUserIdAndProviderIdOrderByRankAsc(userId, providerId);
        if (socialUserConnections.size() > 0) {
            return socialUserConnectionToConnection(socialUserConnections.get(0));
        } else {
            return null;
        }
    }

    private SocialUserConnection connectionToUserSocialConnection(final Connection<?> connection, final Long rank) {
        final ConnectionData connectionData = connection.createData();
        return new SocialUserConnection(
                userId,
                connection.getKey().getProviderId(),
                connection.getKey().getProviderUserId(),
                rank,
                connection.getDisplayName(),
                connection.getProfileUrl(),
                connection.getImageUrl(),
                connectionData.getAccessToken(),
                connectionData.getSecret(),
                connectionData.getRefreshToken(),
                connectionData.getExpireTime()
        );
    }

    private List<Connection<?>> providerUserIdsToConnections(final String providerId,
            final List<String> providerUserIds) {
        final List<SocialUserConnection> socialUserConnections = socialUserConnectionRepository
                .findAllByUserIdAndProviderIdAndProviderUserIdIn(userId, providerId, providerUserIds);
        return socialUserConnectionsToConnections(socialUserConnections);
    }

    private List<Connection<?>> socialUserConnectionsToConnections(
            final List<SocialUserConnection> socialUserConnections) {
        return socialUserConnections.stream()
                .map(this::socialUserConnectionToConnection)
                .collect(Collectors.toList());
    }

    private Connection<?> socialUserConnectionToConnection(final SocialUserConnection socialUserConnection) {
        final ConnectionData connectionData = new ConnectionData(socialUserConnection.getProviderId(),
                socialUserConnection.getProviderUserId(),
                socialUserConnection.getDisplayName(),
                socialUserConnection.getProfileURL(),
                socialUserConnection.getImageURL(),
                socialUserConnection.getAccessToken(),
                socialUserConnection.getSecret(),
                socialUserConnection.getRefreshToken(),
                socialUserConnection.getExpireTime());
        final ConnectionFactory<?> connectionFactory = connectionFactoryLocator
                .getConnectionFactory(connectionData.getProviderId());
        return connectionFactory.createConnection(connectionData);
    }

    private <A> String getProviderId(final Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }
}
