package com.loopers.domain.users;

import com.loopers.interfaces.api.users.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public UserModel signUp(UserV1Dto.UserSignUpRequest request){
        return userRepository.save(request)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[request = " + request + "] 예시를 찾을 수 없습니다."));
    }
}
