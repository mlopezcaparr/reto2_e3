package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController implements  AccountControllerInterface{
    @Autowired
    AccountRepository repo;

    @Override
    public ResponseEntity getCuentaById(long uid, long cid) {
        Account ac = repo.getById(cid);
        if(ac != null & ac.getOwnerId() == uid) return ResponseEntity.status(HttpStatus.OK.value()).body(ac);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
    }

    @Override
    public ResponseEntity getUserCuentas(long uid) {
        List<Account> acs = repo.findByOwnerId(uid);
        if(acs != null & acs.size() > 0) return ResponseEntity.status(HttpStatus.OK.value()).body(acs);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuentas de usuario " + uid + " no encontradas");
    }

    @Override
    public ResponseEntity validateCuenta(long uid, int cant) {
        boolean validated;
        int balanceTotal = repo.getSumAccounts(uid);
        if(cant < balanceTotal*0.8) validated = true;
        else validated = false;
        return ResponseEntity.status(HttpStatus.OK).body(validated);
    }
}
