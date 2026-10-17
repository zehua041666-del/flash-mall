package com.flashmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// scanBasePackages 必须给到 com.flashmall，
// 否则默认只扫启动类所在包（com.flashmall），
// user 模块里的 @Service / @RestController 不会被注册
@SpringBootApplication(scanBasePackages = "com.flashmall")
public class FlashMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlashMallApplication.class, args);
    }
}