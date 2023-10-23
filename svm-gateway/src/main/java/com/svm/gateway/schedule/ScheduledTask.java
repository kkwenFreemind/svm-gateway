package com.svm.gateway.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : Kevin Chang
 * @create 2023/10/19 下午4:24
 */
@Component
@Slf4j
public class ScheduledTask {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void testCron(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        log.info("ScheduledTask datetime:"+ dateFormat.format(new Date()));
    }


}
