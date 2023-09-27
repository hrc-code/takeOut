package com.hrc.takeOut;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public  class TakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakeOutApplication.class, args);
    }

}
