package com.svm.gateway.common;

/**
 * @Author : Kevin Chang
 * @create 2023/9/11 上午9:29
 */
public interface IErrorCode {

    /**
     * Get Code number
     * @return
     */
    long getCode();

    /**
     * Get Message
     * @return
     */
    String getMessage();
}
