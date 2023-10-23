package com.svm.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : Kevin Chang
 * @create 2023/10/18 上午9:18
 */
@Slf4j
@RestController
@RequestMapping("/VM_MD/API")
public class SimulateNotifyController {

    @RequestMapping(value = "/simulate", method = RequestMethod.POST)
    @ResponseBody
    public String getNotify(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password,
            HttpServletRequest request,
            @RequestBody String injson){

        log.info("sim get:"+injson.replaceAll("[ \t\r\n]+", " ").trim());
        return "sim ok";
    }


}
