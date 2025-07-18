package com.loopers.interfaces.api.users;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "User 도메인 관련 Controller 입니다.")
public interface UserV1ApiSpec {

    @Operation(
            summary = "유저 회원가입",
            description = "ID 및 성별, 생년월일, 이메일 주소로 유저 회원가입을 처리합니다."
    )
    //TODO: response 수정, @Schema 수정
    ApiResponse<UserV1Dto.UserSignUpResponse> signUp(
            @Schema(description = "회원가입 요청 정보")
            UserV1Dto.UserSignUpRequest request
    );
}
