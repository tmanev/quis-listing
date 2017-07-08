package com.manev.quislisting.web;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.post.DlListingService;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.ws.rs.NotAuthorizedException;
import java.util.Optional;

@Component
public class MyListingsViewModelComponent {

    private final UserService userService;
    private final DlListingService dlListingService;

    public MyListingsViewModelComponent(UserService userService, DlListingService dlListingService) {
        this.userService = userService;
        this.dlListingService = dlListingService;
    }

    public void fillViewModel(ModelMap modelMap) {

    }

}
