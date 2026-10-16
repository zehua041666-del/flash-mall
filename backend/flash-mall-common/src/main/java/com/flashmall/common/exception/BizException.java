package com.flashmall.common.exception;

import lombok.Getter;


// TODO: BizException 到底为什么需要，我还没完全想清楚，回头补

@Getter
public class BizException extends RuntimeException {
    private final int code;

    public BizException(int code,String message){
        super(message);
        this.code = code;
    }
    public BizException(String message){
        this(500,message);
    }
}
