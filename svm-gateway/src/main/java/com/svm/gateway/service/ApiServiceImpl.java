package com.svm.gateway.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


/**
 * @Author : Kevin Chang
 * @create 2023/10/6 上午10:50
 */
@Slf4j
@Service
public class ApiServiceImpl implements ApiService{

    @Value("${bangmart.url}")
    private String BMart_API_URL;

    @Value("${bangmart.username}")
    private String bMartUsername;

    @Value("${bangmart.password}")
    private String bMartPassword;

//    private RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public ResponseEntity<String> getBangmartInfo(String uri) {
        String url = BMart_API_URL+uri;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("username", bMartUsername);
        headers.add("password", bMartPassword);

        HttpEntity<JSONObject> httpEntity = new HttpEntity<>( headers);
        ResponseEntity<String> result = restTemplate.exchange(url,HttpMethod.GET,httpEntity, String.class);
        log.info("response->"+result.getBody());
        return result;
    }

    @Override
    public ResponseEntity<String> sendNotifyInfo(Object data) {
        String url = "http://localhost:9091/VM_MD/API/simulate";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("username", bMartUsername);
        headers.add("password", bMartPassword);

        log.info("==>"+data);
        HashMap<String,Object> params = new HashMap<>();
        params.put("data",data);

        HttpEntity httpEntity = new HttpEntity<>( params,headers);

        ResponseEntity<String> result = restTemplate.exchange(url,HttpMethod.POST,httpEntity, String.class);
        log.info("response->"+result.getBody());
        return result;
    }

}
