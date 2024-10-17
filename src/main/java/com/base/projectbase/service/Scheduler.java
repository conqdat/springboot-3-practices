package com.base.projectbase.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    // Cron job that runs every second
    @Scheduled(cron = "*/1 * * * * ?")
    public void cronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Cron Job 1 - Every second: " + strDate);
    }

    // Another cron job that runs every 5 seconds
    @Scheduled(cron = "*/5 * * * * ?")
    public void anotherCronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Cron Job 2 - Every 5 seconds: " + strDate);
    }
}
