package com.flashmall.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类：生成 token、解析 token。
 * 放在 common 模块，因为 W7 拆服务后 gateway 也要校验 token，两边共用一份。
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 根据密钥字符串生成 SecretKey。
     * jjwt 0.12.x 要求密钥至少 256 位，否则抛 WeakKeyException。
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 token，payload 里放 userId 和 username。
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))      // subject 存 userId
                .claim("username", username)          // 额外 claim 存 username
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getKey())
                .compact();
    }

    /**
     * 解析 token，返回 Claims（payload）。
     * token 无效 / 过期 → 抛 JwtException，调用方自己 catch。
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 token 里取 userId。
     */
    public Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    /**
     * 从 token 里取 username。
     */
    public String getUsername(String token) {
        return parseToken(token).get("username", String.class);
    }
}