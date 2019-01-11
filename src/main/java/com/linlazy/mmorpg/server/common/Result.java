package com.linlazy.mmorpg.server.common;

/**
 * 业务结果类
 * @author linlazy
 */
public class Result<T> {

    private static final String SUCCESS = "操作成功";

    private String code;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public static Result valueOf(String failCode){
        Result result = new Result();
        result.setCode(failCode);
        return result;
    }

    public boolean isFail(){
        return !code.equals(SUCCESS);
    }
}
