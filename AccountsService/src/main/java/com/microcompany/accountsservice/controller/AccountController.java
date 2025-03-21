package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.payload.ApiResponse;
import com.microcompany.accountsservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController implements AccountControllerInterface {
    @Autowired
    AccountService serv;

    @Override
    public ResponseEntity getCuentaById(long uid, long cid) {
        Account ac = serv.getAccount(cid);
        if (ac != null & ac.getOwnerId() == uid) return ResponseEntity.status(HttpStatus.OK.value()).body(ac);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
    }

    @Override
    public ResponseEntity getUserCuentas(long uid) {
        List<Account> acs = serv.getAccountByOwnerId(uid);
        if (acs != null & acs.size() > 0) return ResponseEntity.status(HttpStatus.OK.value()).body(acs);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuentas de usuario " + uid + " no encontradas");
    }

    @Override
    public ResponseEntity validateCuenta(long uid, int cant) {
        boolean validated = isValidated(uid, cant);
        return ResponseEntity.status(HttpStatus.OK).body(validated);
    }

    private boolean isValidated(long uid, int cant) {
        boolean validated;
        int balanceTotal = serv.getUserBalance(uid);
        if (cant < balanceTotal * 0.8) validated = true;
        else validated = false;
        return validated;
    }

    @Override
    public ResponseEntity addToAccount(Long ownerid, Long aid, int cantidad) {
        Account account = serv.addBalance(aid, cantidad, ownerid);
        if (!account.equals(null)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.NOT_MODIFIED);
        }
    }

    @Override
    public ResponseEntity removeFromAccount(Long ownerid, Long aid, int cantidad) {
        Account account = serv.getAccount(aid);

        if (isValidated(account.getOwnerId(), cantidad)) {
            serv.withdrawBalance(aid, cantidad, ownerid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return new ResponseEntity<>(new ApiResponse("El balance superá el 80 por ciento", false),
                    HttpStatus.NOT_MODIFIED);
        }
    }

    @Override
    public ResponseEntity updateAccount(Long aid, Account account) {
        serv.updateAccount(aid, account);
        if (!account.equals(null)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.NOT_MODIFIED);
        }
    }

    @Override
    public ResponseEntity createAccount(Account account) {
        if (isValidated(account.getOwnerId(), account.getBalance())) {
            serv.create(account);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(new ApiResponse(String.format("User %d cannot create account with balance %d", account.getOwnerId(), account.getBalance()), false), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity deleteAccount(Long uid, Long cuentaId) {
        Account cuenta = null;
        try {
            cuenta = serv.getAccount(cuentaId);
            if (cuenta.getOwnerId() != uid) {
                return new ResponseEntity(new ApiResponse(String.format("No account with id %d for userId %d", cuentaId, uid), false), HttpStatus.BAD_REQUEST);
            }
            serv.delete(cuentaId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (AccountNotfoundException e) {
            return new ResponseEntity<>(new ApiResponse("No account found with id " + cuentaId, false), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity deleteUserAccounts(Long uid) {
        List<Account> userAccounts = serv.getAccountByOwnerId(uid);
        if (userAccounts.isEmpty() || userAccounts.size() < 1) {
            return new ResponseEntity(new ApiResponse("No accounts found for userId " + uid, false), HttpStatus.NOT_FOUND);
        }
        serv.deleteAccountsUsingOwnerId(uid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
