package com.example.springbootpratices.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FakeSendEmailService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

     @Scheduled(fixedRate = 1000) // this function will execute per second
    // @Scheduled(fixedDelay = 5000) // this function will execute delay 5s
//    @Scheduled(cron = "0 15 10 15 * ?") // to run at 10:15 AM on the 15th day of every month
    public void execute() throws InterruptedException {
        System.out.println("this is my code");
         System.out.println("--------------");
        // some logic that will be executed on a schedule
//        Thread.sleep(3000);
//        System.out.println("Code is being executed... Time: " + formatter.format(LocalDateTime.now()));
    }


    @Scheduled(fixedRate = 1001)
    public void test() {
        System.out.println("this is my test");
    }
}
