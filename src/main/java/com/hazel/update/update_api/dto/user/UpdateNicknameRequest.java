package com.hazel.update.update_api.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNicknameRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 10, message = "닉네임은 최대 10자까지 작성 가능합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]+$", message = "띄어쓰기를 없애주세요")
    private String nickname;

}
