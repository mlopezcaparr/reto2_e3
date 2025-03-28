package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.Customer;
import com.microcompany.accountsservice.payload.AccountSimpleReqDto;
import com.microcompany.accountsservice.payload.AccountUpdateDto;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountService implements IAccountService {
    private Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account create(AccountSimpleReqDto accountReq) {
        Account account = new Account();
        BeanUtils.copyProperties(accountReq, account);
        Date current_Date = new Date();
        account.setOpeningDate(current_Date);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null; // Will be gotten from user service
        account.setOwner(owner);
        return account;
    }

    @Override
    public List<Account> getAccountByOwnerId(Long ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    @Override
    public Account updateAccount(Long id, AccountUpdateDto accountReq) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        newAccount.setType(accountReq.getType());
        return accountRepository.save(newAccount);
    }

    @Override
    public Account addBalance(Long id, int amount, Long ownerId) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        if(!ownerId.equals(newAccount.getOwnerId())) {
            throw new AccountNotfoundException("Account not found");
        } else {
            Customer owner = null;// Will be gotten from user service
            int newBalance = newAccount.getBalance() + amount;
            newAccount.setBalance(newBalance);
        }
        return accountRepository.save(newAccount);
    }

    @Override
    public Account withdrawBalance(Long id, int amount, Long ownerId) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        if(!ownerId.equals(newAccount.getOwnerId())) {
            throw new AccountNotfoundException("Account not found");
        } else {
            Customer owner = null; // Will be gotten from user service
            int newBalance = newAccount.getBalance() - amount;
            newAccount.setBalance(newBalance);
        }
        return accountRepository.save(newAccount);
    }

    @Override
    public void delete(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        this.accountRepository.delete(account);
    }

    @Override
    public Integer getUserBalance(Long ownerId) {
        return accountRepository.getSumAccounts(ownerId);
    }

    @Override
    public void deleteAccountsUsingOwnerId(Long ownerId) {
        List<Account> accounts = accountRepository.findByOwnerId(ownerId);
        for (Account account : accounts) {
            this.accountRepository.delete(account);
        }
    }

    public boolean isValidated(long uid, int cant) {
        boolean validated;
        int balanceTotal = getUserBalance(uid);
        if (cant < balanceTotal * 0.8) validated = true;
        else validated = false;
        return validated;
    }
}
