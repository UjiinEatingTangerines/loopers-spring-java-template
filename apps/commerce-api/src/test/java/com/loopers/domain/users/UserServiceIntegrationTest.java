package com.loopers.domain.users;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.loopers.application.users.UserFacade;
import com.loopers.interfaces.api.users.UserV1Dto;
import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserFacade userFacade;

    @SpyBean
    private UserRepository userRepository;

    @DisplayName("회원 가입시 User 저장이 수행된다. ( spy 검증 )")
    @Test
    public void saveUser_whenSignUpIsSuccessful() {
        // given
        UserV1Dto.UserSignUpRequest request = new UserV1Dto.UserSignUpRequest(
                "testUserId",
                "MALE",
                "2000-01-01",
                "test@naver.com"
        );

        // when
        userFacade.signUp(request);

        // then
        //verify(userRepository, times(1)).save(any());
        verify(userRepository).save(request);
    }

    @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
    @Test
    public void throwsException_whenUserIsAlreadyRegistered() {
        // given
        UserV1Dto.UserSignUpRequest request = new UserV1Dto.UserSignUpRequest(
                "testUserId",
                "MALE",
                "2000-01-01",
                "test@naver.com"
        );

        // when
        userFacade.signUp(request); // 첫 번째 가입

        // then
        assertThrows(CoreException.class, () -> {
            userFacade.signUp(request); // 중복 가입 시도
        });
    }
}
