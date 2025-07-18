package com.loopers.application.users;

import com.loopers.domain.users.UserModel;

public record UserInfo (String userId, String gender, String birthDate, String email){
    public static UserInfo from(UserModel model) {
        return new UserInfo(
                model.getUserId(),
                model.getGender(),
                model.getBirthDate(),
                model.getEmail()
        );
    }
}
