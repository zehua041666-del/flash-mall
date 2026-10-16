package com.flashmall.common.result ;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String messaage;
    private T data;
    
    public static <T> Result<T> ok (T data){
        Result<T> r = new Result<>();
        r.code = 0 ;
        r.messaage = "ok";
        r.data =data;
        return r;
    }
    public static <T> Result<T> fail(int code,String message){
        Result<T> r = new Result<>();
        r.code = code;
        r.messaage = message;
        return  r;
    }
}