package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.service.model.UserBaseModel;
import org.springframework.stereotype.Component;

@Component
public class UserBaseModelMapper {

    public UserBaseModel map(User user) {
        UserBaseModel userBaseModel = new UserBaseModel();
        userBaseModel.setId(user.getId());
        userBaseModel.setFirstName(user.getFirstName());
        userBaseModel.setLastName(user.getLastName());
        return userBaseModel;
    }

}
