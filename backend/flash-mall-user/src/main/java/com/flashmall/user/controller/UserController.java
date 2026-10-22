package com.flashmall.user.controller;

import com.flashmall.common.result.Result;
import com.flashmall.user.dto.request.LoginRequest;
import com.flashmall.user.dto.request.RegisterRequest;
import com.flashmall.user.dto.response.LoginResponse;
import com.flashmall.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        // @Valid 触发 DTO 上的校验注解，不通过直接抛 MethodArgumentNotValidException
        // 被 GlobalExceptionHandler 接住，返回 400 + 具体提示，不会进入方法体
        userService.register(request);
        return Result.ok(null);
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(userService.login(request));
    }
}