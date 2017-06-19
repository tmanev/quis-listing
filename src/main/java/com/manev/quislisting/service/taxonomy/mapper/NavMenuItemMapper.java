package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.discriminator.NavMenuItem;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.dto.RefItem;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.NavMenuItemDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

@Component
public class NavMenuItemMapper {

    private PostRepository<AbstractPost> postRepository;
    private UserRepository userRepository;

    public NavMenuItemMapper(PostRepository<AbstractPost> postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private NavMenuItem navMenuItemDtoToNavMenuItem(NavMenuDTO navMenuDTO, NavMenuItemDTO navMenuItemDTO) {
        NavMenuItem navMenuItem = new NavMenuItem();

        navMenuItem.setId(navMenuItemDTO.getId());
        navMenuItem.setTitle(navMenuItemDTO.getTitle());
        navMenuItem.setName(navMenuDTO.getTerm().getSlug() + "-" + navMenuItemDTO.getRefItem().getId() + "-" + navMenuItemDTO.getOrder());
        navMenuItem.setContent("");
        navMenuItem.setStatus(NavMenuItem.Status.PUBLISH);
        navMenuItem.setCommentCount(0L);
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        navMenuItem.setCreated(currentDateTime);
        navMenuItem.setModified(currentDateTime);
        navMenuItem.setPostOrder(navMenuItemDTO.getOrder());
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> oneByLogin = userRepository.findOneByLogin(currentUserLogin);
        if (oneByLogin.isPresent()) {
            navMenuItem.setUser(oneByLogin.get());
        }else {
            throw new UsernameNotFoundException("User " + currentUserLogin + " was not found in the " +
                    "database");
        }

        AbstractPost abstractPost = postRepository.findOne(navMenuItemDTO.getRefItem().getId());
        navMenuItem.setRefPost(abstractPost);

        return navMenuItem;
    }

    public Set<NavMenuItem> navMenuItemDtoToNavMenuItem(NavMenuDTO navMenuDTO, List<NavMenuItemDTO> navMenuItemDTOS) {
        Set<NavMenuItem> navMenuItems = new HashSet<>();

        for (NavMenuItemDTO navMenuItemDTO : navMenuItemDTOS) {
            navMenuItems.add(navMenuItemDtoToNavMenuItem(navMenuDTO, navMenuItemDTO));
        }

        return navMenuItems;
    }

    public NavMenuItemDTO navMenuItemToNavMenuItemDto(NavMenuItem navMenuItem) {
        NavMenuItemDTO navMenuItemDTO = new NavMenuItemDTO();
        navMenuItemDTO.setId(navMenuItem.getId());
        navMenuItemDTO.setTitle(navMenuItem.getTitle());
        navMenuItemDTO.setOrder(navMenuItem.getPostOrder());
        AbstractPost refPost = navMenuItem.getRefPost();
        if (refPost != null) {
            navMenuItemDTO.setRefItem(new RefItem(refPost.getId(), refPost.getName(), refPost.getTitle()));
        }

        return navMenuItemDTO;
    }

    public List<NavMenuItemDTO> navMenuItemToNavMenuItemDto(Set<NavMenuItem> navMenuItemList) {
        List<NavMenuItemDTO> navMenuItemDTOS = new ArrayList<>();
        if (navMenuItemList != null) {
            for (NavMenuItem navMenuItem : navMenuItemList) {
                navMenuItemDTOS.add(navMenuItemToNavMenuItemDto(navMenuItem));
            }
        }
        return navMenuItemDTOS;
    }

}
