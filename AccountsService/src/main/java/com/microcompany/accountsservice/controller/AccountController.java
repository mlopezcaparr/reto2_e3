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
        else throw new AccountNotfoundException("Account not found:");
    }

    @Override
    public ResponseEntity getUserCuentas(long uid) {
        List<Account> acs = serv.getAccountByOwnerId(uid);
        if (acs != null & acs.size() > 0) return ResponseEntity.status(HttpStatus.OK.value()).body(acs);
        else throw new AccountNotfoundException("Accounts of user with id: " + uid + " not found");
    }

    @Override
    public ResponseEntity validateCuenta(long uid, int cant) {
        if(serv.isValidatedAmount(uid, cant)) return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Valid", true));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Not valid", false));
    }

    @Override
    public ResponseEntity addToAccount(Long uid, Long cid, int cant) {
        serv.addBalance(cid, cant, uid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

    @Override
    public ResponseEntity removeFromAccount(Long uid, Long cid, int cant) {
        Account account = serv.getAccount(cid);
        if (serv.isValidatedAmount(account.getOwnerId(), cant)) {
            serv.withdrawBalance(cid, cant, uid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("Cannot withdraw due to the balance exceeds the 80 percent of the loan", false));
        }
    }

    @Override
    public ResponseEntity updateAccount(Long cid, Account ac) {
        serv.updateAccount(cid, ac);
        if (!ac.equals(null)) return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Account can't be null", false));
    }

    @Override
    public ResponseEntity createAccount(Account ac) {
        if (serv.isValidatedAmount(ac.getOwnerId(), ac.getBalance())) {
            serv.create(ac);
            return ResponseEntity.status(HttpStatus.CREATED.value()).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(String.format(
                "User %d cannot create account with balance %d", ac.getOwnerId(), ac.getBalance()), false));
    }

    @Override
    public ResponseEntity deleteAccount(Long uid, Long cid) {
        Account cuenta = serv.getAccount(cid);
        if (cuenta.getOwnerId() != uid)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(String.format(
                    "No account with id %d for userId %d", cid, uid), false));
        else
            serv.delete(cid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

    @Override
    public ResponseEntity deleteUserAccounts(Long uid) {
        List<Account> userAccounts = serv.getAccountByOwnerId(uid);
        if (userAccounts.isEmpty() || userAccounts.size() < 1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No accounts found for userId " + uid, false));
        else
            serv.deleteAccountsUsingOwnerId(uid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }
}
