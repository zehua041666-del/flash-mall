package com.flashmall.user.interceptor;

import com.flashmall.common.context.UserContext;
import com.flashmall.common.exception.BizException;
import com.flashmall.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response ,Object handler) throws Exception{
        String authHeader =request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader)|| !authHeader.startsWith("Bearer ")) {
            throw new BizException(401,"未登录或token已过期");
        }
        String token = authHeader.substring(7);
        try{
            Long userId =jwtUtil.getUserId(token);
            UserContext.setUserId(userId);
            return true;
        }
        catch (Exception e){
            throw new BizException(401,"Token无效或已过期");
        }

    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 7. 请求结束，必须清理 ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
}