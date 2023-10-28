package com.thanksd.server.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class DiaryRequest {
    @NotBlank(message = "2001:일기의 값들은 공백일 수 없습니다.")
    private String content;
    @NotBlank(message = "2001:일기의 값들은 공백일 수 없습니다.")
    private String font;
    @NotBlank(message = "2001:일기의 값들은 공백일 수 없습니다.")
    private String image;
}
