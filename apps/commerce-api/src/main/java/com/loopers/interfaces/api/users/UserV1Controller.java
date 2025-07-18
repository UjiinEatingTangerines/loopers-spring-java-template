package com.loopers.interfaces.api.users;

import com.loopers.application.users.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec{

    //private final UserFacade userFacade;

    @PostMapping
    @Override
    public ApiResponse<UserV1Dto.UserSignUpResponse> signUp(
            @RequestBody UserV1Dto.UserSignUpRequest request
    ) {

        UserInfo info = new UserInfo(
                request.userId(),
                request.gender(),
                request.birthDate(),
                request.email()
        );

        return ApiResponse.success(
                UserV1Dto.UserSignUpResponse.from(info)
        );
    }
}
