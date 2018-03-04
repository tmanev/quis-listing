package com.manev.quislisting.security;

import com.manev.quislisting.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return !StringUtils.isEmpty(userName) ? userName : Constants.SYSTEM_ACCOUNT;
    }
}
