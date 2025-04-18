package com.github.lzpdark.queuewithlocalmessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QueueWithLocalMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueueWithLocalMessageApplication.class, args);
    }

}
