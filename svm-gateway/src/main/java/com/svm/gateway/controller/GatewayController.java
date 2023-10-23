package com.svm.gateway.controller;


import com.svm.gateway.security.AuthUtil;
import com.svm.gateway.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author : Kevin Chang
 * @create 2023/10/6 上午10:37
 */
@Slf4j
@RestController
@RequestMapping("/VM_MD/API")
public class GatewayController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    private ApiService apiService;

    @GetMapping("/machines/machine_names")
    public ResponseEntity<String> getMacineName(@RequestHeader(value = "username") String username,
                                           @RequestHeader(value = "password") String password){

        boolean auth_result = authUtil.checkAccount(username,password);
        if(auth_result) {
            return apiService.getBangmartInfo("machines/machine_names");
        }
        return null;
    }

    @GetMapping("/machines/{machine}/inventory")
    public ResponseEntity<String> getInventoryInfo(@RequestHeader(value = "username") String username,
                                                   @RequestHeader(value = "password") String password,
                                                   @PathVariable Long machine){
        log.info("==>"+machine);
        boolean auth_result = authUtil.checkAccount(username,password);
        if(auth_result) {
            return apiService.getBangmartInfo("/machines/"+machine+"/inventory");
        }
        return null;
    }

    //machines/6501000055/sales?start=2023-04-12T00:00:00&end=2023-05-01T00:00:00
    @GetMapping("/machines/{machine}/sales")
    public ResponseEntity<String> getSales(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password,
            @PathVariable Long machine,
            @RequestParam(value = "start") String startdate,
            @RequestParam(value = "end") String enddate){
        log.info("==>"+machine);
        boolean auth_result = authUtil.checkAccount(username,password);
        if(auth_result) {
            return apiService.getBangmartInfo("/machines/"+machine+"/sales?start="+startdate+"&end="+enddate);
        }
        return null;
    }

    //machines/status?machines=6501000017
    @GetMapping("/machines/status")
    public ResponseEntity<String> getMachineStatus(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password,
            @RequestParam(value = "machines") String machine){

        log.info("==>"+machine);
        boolean auth_result = authUtil.checkAccount(username,password);
        if(auth_result) {
            return apiService.getBangmartInfo("/machines/status?machines="+machine);
        }
        return null;
    }

    //machines/6501000017/status?start=2021-06-25T09:00:00&end=2021-07-09T00:00:00
    @GetMapping("/machines/{machine}/status")
    public ResponseEntity<String> getMachineStatusByDatetime(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password,
            @PathVariable Long machine,
            @RequestParam(value = "start") String startdate,
            @RequestParam(value = "end") String enddate){

        log.info("==>"+machine);
        boolean auth_result = authUtil.checkAccount(username,password);
        if(auth_result) {
            return apiService.getBangmartInfo("/machines/"+machine+"/status?start="+startdate+"&end="+enddate);
        }
        return null;
    }

    //machines/notify?start=2021-07-01T00:00:00&end=2021-07-03T00:00:00
    @GetMapping("/machines/notify")
    public ResponseEntity<String> getMachineStatusByDatetime(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password,
            @RequestParam(value = "start") String startdate,
            @RequestParam(value = "end") String enddate){

        boolean auth_result = authUtil.checkAccount(username,password);
        if(auth_result) {
            return apiService.getBangmartInfo("/machines/notify?start="+startdate+"&end="+enddate);
        }
        return null;
    }


}
