package com.loopers.interfaces.api.users;

import com.loopers.application.users.UserInfo;
import jakarta.validation.constraints.NotBlank;

public class UserV1Dto {

    public record UserSignUpRequest(String userId, String gender, String birthDate, String email) {
    }

    public record UserSignUpResponse(String userId, String gender, String birthDate, String email) {
        public static UserSignUpResponse from(UserInfo info) {
            return new UserSignUpResponse(
                    info.userId(),
                    info.gender(),
                    info.birthDate(),
                    info.email()
            );
        }
    }
}
