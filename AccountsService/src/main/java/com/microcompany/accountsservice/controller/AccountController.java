package com.microcompany.accountsservice.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements  AccountControllerInterface{
    @Override
    public String testMapping() {
        return "Mapping funciona";
    }
}
