package com.svm.gateway.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : Kevin Chang
 * @create 2023/10/18 下午4:31
 */
@Slf4j
@Data
public class CommonResult<T> {

    @JsonProperty("Code")
    private long code;

    @JsonProperty("Reason")
    private String message;

    @JsonProperty("Data")
    private T data;

    @JsonProperty("Response_Time")
    private String response_time;


    protected CommonResult() {
    }

    protected CommonResult(long code, String message, T data) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getNow = sdf.format(new Date());

        this.code = code;
        this.message = message;
        this.response_time =getNow;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 獲取的數據
     */
    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> CommonResult<T> failed(String message,T data) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, data);
    }

    public static <T> CommonResult<T> validateFailed(String message,T data) {
        return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message,data);
    }

}
