package ru.social.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RumorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RumorApplication.class);
    }
}
