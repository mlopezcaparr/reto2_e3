package com.microcompany.accountsservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cuentas")
public interface AccountControllerInterface {

    @RequestMapping("")
    public String testMapping();
}
