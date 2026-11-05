package com.flashmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flashmall.common.exception.BizException;
import com.flashmall.common.util.JwtUtil;
import com.flashmall.user.dto.request.LoginRequest;
import com.flashmall.user.dto.request.RegisterRequest;
import com.flashmall.user.dto.response.LoginResponse;
import com.flashmall.user.entity.User;
import com.flashmall.user.mapper.UserMapper;
import com.flashmall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {
        // 先查用户名是否已存在
        // 注意：MyBatis-Plus 逻辑删除会给查询自动追加 WHERE deleted = 0，
        // 所以已逻辑删除的同名用户查不到；但唯一索引 uk_username 依然包含旧行，
        // 直接 insert 会撞索引，因此插入处需要兜底处理重复键。
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())
        );
        if (count > 0) {
            throw new BizException(400, "用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        // BCrypt 加密，绝不能明文入库
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setStatus(1);   // 默认正常

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            // 并发注册，或同名用户已被逻辑删除但唯一索引仍占用
            throw new BizException(400, "用户名已存在");
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())
        );

        // 用户不存在 和 密码错误，故意返回同一个提示
        // 防止攻击者通过错误信息判断"用户名是否存在"（用户名枚举攻击）
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BizException(400, "用户名或密码错误");
        }

        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BizException(403, "账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getNickname());
    }
}