<!-- 시퀀스 다이어그램 최소 2개 이상 (Mermaid 기반 작성 권장) -->


## 1. 상품 목록 조회

```mermaid
sequenceDiagram
    participant User
    participant ProductController
    participant ProductService
    participant LikeService
    participant BrandService

    %% 1. 요청 시작
    User->>ProductController: 상품 목록 조회 요청 (keyword, brandId, X-USER-ID optional)

    %% 2. 검색 조건 유효성 및 분기 처리
    alt X-USER-ID 존재
        ProductController->>+LikeService: 로그인 유저의 좋아요 정보 조회 준비
    else 비회원
        note right of ProductController: 좋아요 조회 생략
    end

    %% 3. 상품 목록 검색 (키워드, 브랜드 필터 포함)
    ProductController->>ProductService: 상품 목록 검색(keyword, brandId)
    ProductService-->>ProductController: 유효한 상품 목록 반환 (삭제 상품 제외)

    %% 4. 상품 반복 처리
    loop 상품 목록 반복
        alt 상품이 품절된 경우
            note right of ProductController: 품절 표시 플래그 추가
        end

    %% 5. 좋아요 여부 확인
        alt X-USER-ID 존재
            ProductController->>LikeService: 좋아요 여부 조회(X-USER-ID, productId)
        else
            note right of ProductController: 비회원 → 좋아요 미표시
        end

        ProductController->>BrandService: 브랜드 정보 조회 (브랜드 ID)
    end

    %% 6. 응답 반환
    ProductController-->>User: 상품 목록 응답 (검색 조건 반영, 품절 표시 포함)


```


## 2. 상품 상세 조회

```mermaid
sequenceDiagram
    participant User
    participant ProductController
    participant ProductService
    participant LikeService

    %% 1. 유저가 상품 상세 요청
    User->>ProductController: 상품 상세 조회 요청 (productId, X-USER-ID optional)

    %% 2. 상품 단건 조회
    ProductController->>ProductService: 상품 단건 조회(productId)

    %% 3. 삭제 여부 확인
    alt 상품이 삭제된 경우
        note right of ProductController: "품절 팝업" → 메인 페이지로 리디렉션
        ProductController-->>User: 삭제됨 응답 + redirect
    else
    %% 4. 품절 여부 확인
        alt 상품이 품절된 경우
            note right of ProductController: 상세 페이지에 "품절 표시" + 주문 불가
        end

    %% 5. 좋아요 여부 확인
        alt X-USER-ID 존재
            ProductController->>LikeService: 좋아요 여부 조회(X-USER-ID, productId)
        else
            note right of ProductController: 비회원 → 좋아요 미표시
        end

    %% 6. 상품 응답 반환
        ProductService-->>ProductController: 상품 상세 데이터
        ProductController-->>User: 상품 상세 응답 (품절 여부 + 좋아요 여부 포함)
    end

```

## 3. 브랜드 정보 조회

```mermaid
sequenceDiagram
    participant User
    participant BrandController
    participant BrandService

    %% 1. 유저가 브랜드 검색 또는 상세 조회 요청
    User->>BrandController: 브랜드 상세 정보 조회 요청 (brandId or brandName)

    %% 2. 브랜드 조회
    BrandController->>BrandService: 브랜드 정보 조회 (brandId or brandName)

    %% 3. 예외 처리
    alt 브랜드가 존재하지 않음 or 삭제됨
        note right of BrandController: "브랜드 준비 중" 메시지 응답
        BrandController-->>User: 에러 응답 (브랜드 없음)
    else
        BrandService-->>BrandController: 브랜드 상세 정보 반환
        BrandController-->>User: 브랜드 상세 정보 응답
    end

```

## 4. 브랜드별 상품 목록 조회

```mermaid
sequenceDiagram
    participant User
    participant BrandController
    participant BrandService
    participant ProductService
    participant LikeService

    %% 1. 유저가 브랜드 상품 목록 요청
    User->>BrandController: 브랜드 상품 목록 조회 요청 (brandId, X-USER-ID optional)

    %% 2. 브랜드 유효성 검증
    BrandController->>BrandService: 브랜드 존재 여부 확인(brandId)
    alt 브랜드가 존재하지 않음 or 삭제됨
        note right of BrandController: "브랜드 준비 중" 메시지 반환
        BrandController-->>User: 에러 응답 (브랜드 없음)
    else
    %% 3. 브랜드 상품 목록 조회
        BrandController->>ProductService: 브랜드 상품 목록 조회(brandId)
        alt 상품이 존재하지 않음
            note right of BrandController: "브랜드 준비 중" 메시지 반환
            BrandController-->>User: 에러 응답 (상품 없음)
        else
        %% 4. 좋아요 여부 확인 (로그인 유저만)
            alt X-USER-ID 존재
                BrandController->>LikeService: 유저 좋아요 정보 조회 (X-USER-ID, 상품 ID 목록)
            else
                note right of BrandController: 비회원 → 좋아요 미표시
            end

        %% 5. 결과 응답
            ProductService-->>BrandController: 상품 목록 데이터
            BrandController-->>User: 브랜드 상품 목록 응답 (간략 정보 + 좋아요 여부)
        end
    end

```

## 5. 브랜드별 상품 조회

```mermaid
sequenceDiagram
    participant User
    participant BrandController
    participant BrandService
    participant ProductService
    participant LikeService

    %% 1. 유저가 브랜드 상품 단건 조회 요청
    User->>BrandController: 브랜드 상품 단건 조회 요청 (brandId, productId, X-USER-ID optional)

    %% 2. 브랜드 존재 및 유효성 검증
    BrandController->>BrandService: 브랜드 유효성 검증 (brandId)
    alt 브랜드가 존재하지 않음 or 삭제됨
        note right of BrandController: "브랜드 준비 중" 메시지 응답
        BrandController-->>User: 에러 응답 (브랜드 없음)
    else
    %% 3. 상품 단건 조회
        BrandController->>ProductService: 상품 단건 조회(productId)

        alt 상품이 삭제됨
            note right of BrandController: "품절 팝업" → 메인 페이지 리디렉션
            BrandController-->>User: 삭제 응답 + redirect
        else
            alt 상품이 품절됨
                note right of BrandController: 상세 페이지에 "품절 표시" + 주문 불가
            end

        %% 4. 좋아요 여부 조회 (로그인 유저일 경우)
            alt X-USER-ID 존재
                BrandController->>LikeService: 좋아요 여부 조회 (X-USER-ID, productId)
            else
                note right of BrandController: 비회원 → 좋아요 미표시
            end

        %% 5. 응답 반환
            ProductService-->>BrandController: 상품 상세 데이터
            BrandController-->>User: 상품 단건 응답 (브랜드 상품 정보 + 품절 여부 + 좋아요 여부)
        end
    end

```

## 6. 상품 좋아요 등록

```mermaid
sequenceDiagram
    participant User
    participant ProductController
    participant LikeService

    %% 1. 좋아요 요청
    User->>ProductController: 좋아요 요청 (productId, X-USER-ID)

    %% 2. 로그인 여부 확인
    alt X-USER-ID 없음 (비회원)
        note right of ProductController: "로그인 필요" 에러 응답
        ProductController-->>User: 401 Unauthorized (비회원)
    else
    %% 3. 좋아요 상태 확인
    ProductController->>LikeService: 좋아요 등록 요청 (userId, productId)

    note right of LikeService: 좋아요 등록 처리
    LikeService-->>ProductController: 좋아요 등록됨
        

    %% 4. 응답 반환
        ProductController-->>User: 좋아요 등록/취소 결과 응답
    end

```

## 7. 상품 좋아요 취소

```mermaid
sequenceDiagram
    participant User
    participant ProductController
    participant LikeService

    %% 1. 좋아요 취소 요청
    User->>ProductController: 좋아요 취소 요청 (productId, X-USER-ID)

    %% 2. 로그인 여부 확인
    alt X-USER-ID 없음 (비회원)
        note right of ProductController: "로그인 필요" 에러 응답
        ProductController-->>User: 401 Unauthorized
    else
    %% 3. 좋아요 상태 확인
        ProductController->>LikeService: 좋아요 여부 확인(userId, productId)

        alt 좋아요가 존재함
            note right of LikeService: 좋아요 삭제 처리
            LikeService-->>ProductController: 성공 응답 (좋아요 취소됨)
        end

    %% 4. 최종 응답
        ProductController-->>User: 좋아요 취소 결과 응답
    end

```

## 8. 좋아요 한 상품 목록 조회

```mermaid
sequenceDiagram
    participant User
    participant LikeController
    participant LikeService
    participant ProductService

    %% 1. 유저가 좋아요 목록 페이지 진입
    User->>LikeController: 좋아요 상품 목록 조회 요청 (X-USER-ID)

    %% 2. 로그인 여부 확인
    alt X-USER-ID 없음
        note right of LikeController: "로그인 필요" 응답
        LikeController-->>User: 401 Unauthorized
    else
    %% 3. 유저의 좋아요 상품 ID 목록 조회
        LikeController->>LikeService: 좋아요 상품 ID 목록 조회(userId)

        alt 좋아요한 상품 없음
            note right of LikeController: "좋아요한 상품이 없어요" 메시지
            LikeController-->>User: 빈 목록 응답
        else
        %% 4. 상품 간략 정보 조회
            LikeService-->>LikeController: 좋아요된 상품 ID 목록
            LikeController->>ProductService: 상품 정보 일괄 조회(List<productId>)
            ProductService-->>LikeController: 상품 요약 정보 리스트

        %% 5. 응답 반환
            LikeController-->>User: 좋아요 상품 목록 응답
        end
    end

```

## 9. 주문 요청

```mermaid
sequenceDiagram
    participant User
    participant OrderController
    participant ProductService
    participant PointService
    participant OrderService

    %% 1. 주문 요청
    User->>OrderController: 상품 주문 요청 (productId, X-USER-ID)

    %% 2. 로그인 여부 확인
    alt X-USER-ID 없음
        note right of OrderController: 로그인 필요
        OrderController-->>User: 401 Unauthorized
    else
    %% 3. 상품 상태 확인
        OrderController->>ProductService: 상품 상태 조회(productId)
        alt 상품이 삭제됨 or 품절됨
            note right of OrderController: 주문 불가 처리
            OrderController-->>User: 400 BadRequest (상품 주문 불가)
        else
        %% 4. 포인트 보유액 확인
            OrderController->>PointService: 유저 포인트 조회(userId)
            alt 포인트 부족
                note right of OrderController: 주문 실패 - 포인트 부족
                OrderController-->>User: 400 BadRequest (포인트 부족)
            else
            %% 5. 주문 처리
                OrderController->>OrderService: 주문 생성 요청(userId, productId)
                OrderService-->>OrderController: 주문 생성 완료

            %% 6. 응답
                OrderController-->>User: 주문 성공 응답
            end
        end
    end

```

## 10. 주문 요청 목록 조회 

```mermaid
sequenceDiagram
    participant User
    participant OrderController
    participant OrderService
    participant ProductService

    %% 1. 유저가 주문 목록 페이지 접근
    User->>OrderController: 주문 목록 조회 요청 (X-USER-ID)

    %% 2. 로그인 여부 확인
    alt X-USER-ID 없음
        note right of OrderController: "로그인 필요"
        OrderController-->>User: 401 Unauthorized
    else
    %% 3. 주문 목록 조회
        OrderController->>OrderService: 주문 목록 조회(userId)

        alt 주문이 1건도 없음
            note right of OrderController: "주문한 상품이 없습니다" 메시지 반환
            OrderController-->>User: 빈 목록 응답
        else
        %% 4. 주문에 해당하는 상품 정보 조회 (옵션)
            OrderService-->>OrderController: 주문 목록 데이터
            OrderController->>ProductService: 상품 정보 일괄 조회(List<productId>)
            ProductService-->>OrderController: 상품 요약 정보 목록

        %% 5. 최종 응답
            OrderController-->>User: 주문 목록 응답 (상품 정보 포함)
        end
    end

```

## 10. 주문 요청 상세 조회 

```mermaid
sequenceDiagram
    participant User
    participant OrderController
    participant OrderService
    participant ProductService

    %% 1. 유저가 단일 주문 상세 요청
    User->>OrderController: 주문 상세 조회 요청 (orderId, X-USER-ID)

    %% 2. 로그인 여부 확인
    alt X-USER-ID 없음
        note right of OrderController: 로그인 필요
        OrderController-->>User: 401 Unauthorized
    else
    %% 3. 주문 단건 조회 및 소유자 검증
        OrderController->>OrderService: 주문 단건 조회(orderId)
        alt 주문이 존재하지 않음
            note right of OrderController: 404 Not Found
            OrderController-->>User: 주문 없음 응답
        else
            OrderService-->>OrderController: 주문 데이터
            alt 주문의 userId != X-USER-ID
                note right of OrderController: 403 Forbidden
                OrderController-->>User: 권한 없음 응답
            else
            %% 4. 상품 정보 보강 (선택적)
                OrderController->>ProductService: 상품 정보 조회(productId)
                ProductService-->>OrderController: 상품 상세 데이터

            %% 5. 응답 반환
                OrderController-->>User: 주문 상세 응답 (상품 정보 포함)
            end
        end
    end

```