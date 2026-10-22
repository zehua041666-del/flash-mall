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
        // MyBatis-Plus 逻辑删除：这里自动带 WHERE deleted = 0，已删的用户名可以复用
        //这里是MP的实际应用 我现在不太清楚 所以 现在我先用我的理解来描述一下 这个的过程 首先就是 他是直接先看request.getusername
        //然后 后面是
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

        userMapper.insert(user);
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