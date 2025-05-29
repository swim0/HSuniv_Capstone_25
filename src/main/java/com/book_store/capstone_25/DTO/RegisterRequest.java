package com.book_store.capstone_25.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequest {

    private String userId;

    /** 6~10자 비밀번호 */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 15, message = "비밀번호는 6~15자여야 합니다.")
    private String password;

    /** 비밀번호 확인 */
    @NotBlank(message = "비밀번호 확인을 입력하세요.")
    private String confirmPassword;

    /* --------------- 선택: 회원가입 폼의 나머지 필드 --------------- */
    @NotBlank(message =  "이름을 입력해주세요.")
    private String name;

    @Positive(message = "나이는 양의 정수여야 합니다.")
    private Integer age;

    @Email(message = "유효한 이메일 주소가 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Size(max = 11, message = "휴대폰 번호를 입력해주세요")
    @NotBlank
    private String phoneNumber;

    @NotBlank(message = "주소를 입력해주세요")
    private String address;
    @NotBlank(message = "생일을 설정해주세요")
    private String birthDate;

    /* ------------ ▲ 이 부분은 프로젝트 요구사항에 따라 추가/제거 ------------ */

    /** DTO 내부 검증: 비밀번호 일치 */
    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }
}
