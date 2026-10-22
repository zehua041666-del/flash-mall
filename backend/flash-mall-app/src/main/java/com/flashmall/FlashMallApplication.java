package com.flashmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// scanBasePackages 必须给到 com.flashmall，
// 否则默认只扫启动类所在包（com.flashmall），
// user 模块里的 @Service / @RestController 不会被注册
@SpringBootApplication(scanBasePackages = "com.flashmall")
// 一次扫全部模块的 mapper 包，后续新增 Mapper 不用再单独标 @Mapper
@MapperScan("com.flashmall.**.mapper")
public class FlashMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlashMallApplication.class, args);
    }
}