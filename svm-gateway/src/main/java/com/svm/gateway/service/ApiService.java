package com.svm.gateway.service;

import org.springframework.http.ResponseEntity;

/**
 * @Author : Kevin Chang
 * @create 2023/10/6 上午10:49
 */
public interface ApiService {


    ResponseEntity<String> getBangmartInfo(String uri);

    ResponseEntity<String> sendNotifyInfo(Object data);


}
