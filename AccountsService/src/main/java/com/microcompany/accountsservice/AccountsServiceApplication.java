package com.microcompany.accountsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountsServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AccountsServiceApplication.class, args);
    }
}
