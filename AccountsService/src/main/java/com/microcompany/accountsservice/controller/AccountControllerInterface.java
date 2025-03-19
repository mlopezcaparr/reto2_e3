package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cuentas")
public interface AccountControllerInterface {
    @RequestMapping("")
    public String testMapping();

    @RequestMapping("/{id}")
    public Account getCuentaById(@PathVariable("id") long cid);
}
