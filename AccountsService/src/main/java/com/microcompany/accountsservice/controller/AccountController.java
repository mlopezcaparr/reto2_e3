package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements  AccountControllerInterface{
    @Autowired
    AccountRepository repo;
    @Override
    public String testMapping() {
        return "Mapping funciona";
    }

    @Override
    public Account getCuentaById(long cid) {
        return repo.getById(cid);
    }
}
