package com.microcompany.accountsservice.exception;

public class AccountNotfoundException extends GlobalException {
    protected static final long serialVersionUID = 2L;

    public AccountNotfoundException(String message) {super(message);}

    public AccountNotfoundException(Long accountId) {
        super("Account with id: " + accountId + " not found");
    }
}
