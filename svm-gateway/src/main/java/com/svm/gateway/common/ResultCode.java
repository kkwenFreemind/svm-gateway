package com.svm.gateway.common;

/**
 * @Author : Kevin Chang
 * @create 2023/9/11 上午9:29
 */
public enum ResultCode implements IErrorCode{

    SUCCESS(0, "成功"),
    FAILED(500, "失敗"),
    VALIDATE_FAILED(404, "參數有誤");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
