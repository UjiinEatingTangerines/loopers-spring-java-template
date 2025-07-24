package com.loopers.infrastructure.users;

import com.loopers.domain.users.UserModel;
import com.loopers.domain.users.UserRepository;
import com.loopers.interfaces.api.users.UserV1Dto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserModel> save(UserV1Dto.UserSignUpRequest request) {
        UserModel userModel = new UserModel(
            request.userId(),
            request.gender(),
            request.birthDate(),
            request.email()
        );
        return Optional.of(userJpaRepository.save(userModel));
    }

    @Override
    public boolean existsByUserId(String userId) {
        return userJpaRepository.existsByUserId(userId);
    }
}
