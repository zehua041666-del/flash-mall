package com.flashmall.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录成功后返回给前端的数据。
 * token 用于后续请求的 Authorization header，
 * 其余字段方便前端直接展示，不用再单独查一次用户信息。
 */
@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private Long userId;

    private String username;

    private String nickname;
}