package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import com.microcompany.accountsservice.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTestMockMvcTest {

    @BeforeEach
    public void setUp() {
        List<Account> accounts = List.of(
                new Account(1L, "Company", Date.from(LocalDate.parse("2023-03-25")
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()), 1000, 1L, null),
                new Account(2L, "Personal", Date.from(LocalDate.parse("2022-06-21")
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()), 2000, 1L, null),
                new Account(3L, "Personal", Date.from(LocalDate.parse("2003-08-11")
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()), 800, 2L, null),
                new Account(4L, "Company", Date.from(LocalDate.parse("2004-11-03")
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()), 1500, 3L, null),
                new Account(5L, "Company", Date.from(LocalDate.parse("2023-02-01")
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()), 1200, 3L, null)
        );

        List<Account> accountFilter;

        accountFilter = accounts.stream().filter(account -> account.getOwnerId().equals(1L)).collect(Collectors.toList());
        Mockito.when(accountServiceMock.getAccountByOwnerId(1L)).thenReturn(accountFilter);
        Mockito.when(accountServiceMock.addBalance(1L, 200, 1L)).thenReturn(accounts.get(1));
        Mockito.when(accountServiceMock.addBalance(1L, 200, 4L)).thenThrow(AccountNotfoundException.class);
        Mockito.when(accountServiceMock.create(Mockito.any(Account.class)))
                .thenAnswer(elem -> {
                    Account accountCreated = (Account) elem.getArguments()[0];
                    accountCreated.setId(6L);
                    return accountCreated;
                });
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountServiceMock;

    @MockBean
    private AccountRepository accountRepositoryMock;

    @Test
    void givenLong_WhenSearchForAccounWithOwnerId_ThenSuccess() throws Exception {
        mvc.perform(get("/accounts/1/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].ownerId", is(1)));
    }

    @Test
    void givenAccountData_WhenTryAddAmount_ThenSuccess() throws Exception {
        mvc.perform(put("/accounts/1/add?cid=1&cantidad=200"))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenAccountData_WhenTryAddAmount_ThenExceptionNotAccountFound() throws Exception {
        mvc.perform(put("/accounts/1/add?cid=4&cantidad=200")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void givenNewTypeForAccount_WhenTryToUpdateAccount_ThenSuccess() throws Exception {
        Account updateAccount = new Account(null, "Company", Date.from(LocalDate.parse("2025-01-01")
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), 1000, 1L, null);

        mvc.perform(put("/accounts/1?cid=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(updateAccount))
                )
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isNoContent());
    }

    @Test
    void givenNewTypeForAccount_WhenTryToUpdateToInvalidAccountType_ThenAccountTypeExceptionConstrain() throws Exception {
        Account updateAccount = new Account(null, "aa", Date.from(LocalDate.parse("2025-01-01")
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), 1000, 1L, null);

        mvc.perform(put("/accounts/1?cid=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(updateAccount))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isPreconditionFailed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void givenNewAccount_WhenTryToCreateIt_ThenSuccess() throws Exception {
        Account newAccount = new Account(null, "Personal", Date.from(LocalDate.parse("2004-07-08")
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), 100, 2L, null);

        mvc.perform(post("/accounts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(newAccount))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void givenNewAccount_WhenTryToCreateItWithWrongBalance_ThenFailedToCreate() throws Exception {
        Account newAccount = new Account(null, "Company", Date.from(LocalDate
                .parse("2000-10-01").atStartOfDay(ZoneId.systemDefault()).toInstant()), 99000, 2L, null);

        mvc.perform(post("/accounts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.asJsonString(newAccount))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)));

    }
}
