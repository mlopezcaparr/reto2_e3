package com.microcompany.accountsservice.persistence;

import com.microcompany.accountsservice.model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    AccountRepository repo;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByOwnerId() {
        List<Account> acs = repo.findByOwnerId(2L);
        assertEquals(acs.get(1).getOwnerId(), 2);
        assertTrue(true);
    }

    @Test
    void getSumAccounts() {
        List<Account> acs = repo.findByOwnerId(2L);
        int balance = 0;
        for(Account a : acs){
            balance += a.getBalance();
        }
        assertEquals(balance, repo.getSumAccounts(2L));
    }
}