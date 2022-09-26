package com.abc.eatwell;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class EatWellApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatWellApplication.class, args);
        log.info("successfully launched app");
    }
}