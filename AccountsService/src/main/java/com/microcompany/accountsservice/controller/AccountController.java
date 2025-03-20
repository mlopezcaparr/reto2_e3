package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.payload.ApiResponse;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController implements  AccountControllerInterface{
    @Autowired
    AccountService serv;

    @Override
    public ResponseEntity getCuentaById(long uid, long cid) {
        Account ac = serv.getAccount(cid);
        if(ac != null & ac.getOwnerId() == uid) return ResponseEntity.status(HttpStatus.OK.value()).body(ac);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
    }

    @Override
    public ResponseEntity getUserCuentas(long uid) {
        List<Account> acs =serv.getAccountByOwnerId(uid);
        if(acs != null & acs.size() > 0) return ResponseEntity.status(HttpStatus.OK.value()).body(acs);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuentas de usuario " + uid + " no encontradas");
    }

    @Override
    public ResponseEntity validateCuenta(long uid, int cant) {
        boolean validated;
        int balanceTotal = serv.getUserBalance(uid);
        if(cant < balanceTotal*0.8) validated = true;
        else validated = false;
        return ResponseEntity.status(HttpStatus.OK).body(validated);
    }

    @Override
    public ResponseEntity addToAccount(Long ownerid, Long aid, int cantidad) {
        Account end = serv.addBalance(aid, cantidad, ownerid);
        if (end != null) {
            return  ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.NOT_MODIFIED);
        }
    }

    @Override
    public ResponseEntity removeFromAccount(Long ownerid, Long aid, int cantidad) {
        Account end = serv.withdrawBalance(aid, cantidad, ownerid);
        if (end != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.NOT_MODIFIED);
        }
    }

    @Override
    public ResponseEntity updateAccount(Long aid, Account account) {
        serv.updateAccount(aid, account);
        if (account != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.NOT_MODIFIED);
        }
    }
}
