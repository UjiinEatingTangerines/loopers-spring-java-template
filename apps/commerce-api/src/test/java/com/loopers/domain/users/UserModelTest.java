package com.loopers.domain.users;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserModelTest {

    @DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    @Test
    public void throwsException_whenIdIsNotValid() {
        // given
        String userId = "testUserIdㅇ";
        String gender = "MALE";
        String birthDate = "2000-01-01";
        String email = "testUser@naver.com";

        // when & then
        assertThrows(CoreException.class, () -> {
            new UserModel(userId, gender, birthDate, email);
        });

    }

    @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    @Test
    public void throwsException_whenEmailIsNotValid() {
        // given
        String userId = "testUserId";
        String gender = "MALE";
        String birthDate = "2000-01-01";
        String email = "testUserEmail";

        // when & then
        assertThrows(CoreException.class, () -> {
            new UserModel(userId, gender, birthDate, email);
        });
    }

    @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    @Test
    public void throwsException_whenBirthDateIsNotValid() {
        // given
        String userId = "testUserId";
        String gender = "MALE";
        String birthDate = "2000:01:01";
        String email = "testUserEmail@naver.com";

        // when & then
        assertThrows(CoreException.class, () -> {
            new UserModel(userId, gender, birthDate, email);
        });
    }
}
