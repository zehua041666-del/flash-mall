package com.flashmall.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 项目只引了 spring-security-crypto，没引完整 Spring Security，
 * 所以没有自动配置的 PasswordEncoder Bean，需要手动定义。
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 内置随机 salt，同一个密码每次加密结果都不同，但 matches 能正确校验
        return new BCryptPasswordEncoder();
    }
}