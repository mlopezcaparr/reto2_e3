package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.payload.AccountSimpleReqDto;
import com.microcompany.accountsservice.payload.AccountUpdateDto;

import java.util.List;

public interface IAccountService {
    Account create(AccountSimpleReqDto account);

    List<Account> getAccounts();

    Account getAccount(Long id);

    List<Account> getAccountByOwnerId(Long ownerId);

    Account updateAccount(Long id, AccountUpdateDto account);

    Account addBalance(Long id, int amount, Long ownerId);

    Account withdrawBalance(Long id, int amount, Long ownerId);

    void delete(Long id);

    Integer getUserBalance(Long ownerId);

    void deleteAccountsUsingOwnerId(Long ownerId);

}
