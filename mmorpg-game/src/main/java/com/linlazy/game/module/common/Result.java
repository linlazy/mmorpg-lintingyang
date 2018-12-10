package com.linlazy.game.module.common;

/**
 * 业务结果类
 */
public class Result<T> {

    private static final int SUCCESS = 0;

    private int code;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result valueOf(int failCode){
        Result result = new Result();
        result.setCode(failCode);
        return result;
    }
}
