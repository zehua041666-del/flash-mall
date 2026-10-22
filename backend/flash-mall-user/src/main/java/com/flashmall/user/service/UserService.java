package com.flashmall.user.service;

import com.flashmall.user.dto.request.LoginRequest;
import com.flashmall.user.dto.request.RegisterRequest;
import com.flashmall.user.dto.response.LoginResponse;

public interface UserService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}