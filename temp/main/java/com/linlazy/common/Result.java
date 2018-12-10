package com.linlazy.common;

/**
 * 业务结果类
 */
public class Result {

    private static final int SUCCESS = 0;

    private int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }

    public static Result valueOf(int failCode){
        Result result = new Result();
        result.setCode(failCode);
        return result;
    }
}
