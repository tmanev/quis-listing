package com.manev.quislisting.repository;

import com.manev.quislisting.domain.SocialUserConnection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

/**
 * Custom implementation of {@link UsersConnectionRepository} in order to support social signin.
 */
public class CustomSocialUsersConnectionRepository implements UsersConnectionRepository {

    private final SocialUserConnectionRepository socialUserConnectionRepository;

    private final ConnectionFactoryLocator connectionFactoryLocator;

    public CustomSocialUsersConnectionRepository(final SocialUserConnectionRepository socialUserConnectionRepository,
            final ConnectionFactoryLocator connectionFactoryLocator) {
        this.socialUserConnectionRepository = socialUserConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    @Override
    public List<String> findUserIdsWithConnection(final Connection<?> connection) {
        final ConnectionKey key = connection.getKey();
        final List<SocialUserConnection> socialUserConnections =
                socialUserConnectionRepository
                        .findAllByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
        return socialUserConnections.stream()
                .map(SocialUserConnection::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds) {
        final List<SocialUserConnection> socialUserConnections =
                socialUserConnectionRepository.findAllByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
        return socialUserConnections.stream()
                .map(SocialUserConnection::getUserId)
                .collect(Collectors.toSet());
    }

    @Override
    public ConnectionRepository createConnectionRepository(final String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new CustomSocialConnectionRepository(userId, socialUserConnectionRepository, connectionFactoryLocator);
    }
}
