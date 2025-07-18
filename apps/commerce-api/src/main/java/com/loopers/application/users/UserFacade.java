package com.loopers.application.users;

import com.loopers.domain.users.UserModel;
import com.loopers.domain.users.UserService;
import com.loopers.interfaces.api.users.UserV1Dto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;

    public UserInfo signUp(UserV1Dto.UserSignUpRequest request){
        UserModel user = userService.signUp(request);
        return UserInfo.from(user);
    }
}
