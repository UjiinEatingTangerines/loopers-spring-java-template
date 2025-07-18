package com.loopers.domain.users;

import com.loopers.interfaces.api.users.UserV1Dto;

import java.util.Optional;

public interface UserRepository {
    Optional<UserModel> save(UserV1Dto.UserSignUpRequest request);
}
