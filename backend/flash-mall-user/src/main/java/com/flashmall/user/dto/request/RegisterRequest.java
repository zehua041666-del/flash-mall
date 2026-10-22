package com.flashmall.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册请求体。
 * 用 @NotBlank / @Size / @Email 做参数校验，
 * Controller 里加 @Valid 后，不合法参数会被 GlobalExceptionHandler 的
 * MethodArgumentNotValidException 分支统一处理成 400。
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度需在 3~50 之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度需在 6~50 之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;
}