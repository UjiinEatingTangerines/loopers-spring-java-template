package com.loopers.interfaces.api.users;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserV1ControllerE2ETest {

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ControllerE2ETest(
            TestRestTemplate testRestTemplate,
            DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
    @Test
    void returnsCreatedUser_whenSignUpIsSuccessful() {
        // given
        String userId = "testUserId";
        String gender = "MALE";
        String birthDate = "2000-01-01";
        String email = "testUser@naver.com";

        UserV1Dto.UserSignUpRequest request = new UserV1Dto.UserSignUpRequest(userId, gender, birthDate, email);

        // when
        ParameterizedTypeReference<ApiResponse<UserV1Dto.UserSignUpResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<UserV1Dto.UserSignUpResponse>> response = testRestTemplate.exchange(
                "/api/v1/users",
                HttpMethod.POST,
                new HttpEntity<>(request),
                responseType
        );

        // then
        assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().userId()).isEqualTo(userId),
                () -> assertThat(response.getBody().data().gender()).isEqualTo(gender),
                () -> assertThat(response.getBody().data().birthDate()).isEqualTo(birthDate),
                () -> assertThat(response.getBody().data().email()).isEqualTo(email)
        );
    }

    @DisplayName("회원 가입 시에 성별이 없을 경우, 400 Bad Request 응답을 반환한다.")
    @Test
    void returnsBadRequest_whenGenderIsMissing() {

        // given
        String userId = "testUserId";
        String gender = null;
        String birthDate = "2000-01-01";
        String email = "testUser@naver.com";

        UserV1Dto.UserSignUpRequest request = new UserV1Dto.UserSignUpRequest(userId, gender, birthDate, email);

        // when
        ParameterizedTypeReference<ApiResponse<UserV1Dto.UserSignUpResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<UserV1Dto.UserSignUpResponse>> response = testRestTemplate.exchange(
                "/api/v1/users",
                HttpMethod.POST,
                new HttpEntity<>(request),
                responseType
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}
