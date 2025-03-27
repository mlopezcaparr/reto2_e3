package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.microcompany.accountsservice.util.JsonUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AccountControllerTestMockMvc {

    @Autowired
    private MockMvc mvc;

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
    void givenAccountData_WhenTryRemoveAmount_ThenSuccess() throws Exception {
        mvc.perform(put("/accounts/1/withdraw?cid=1&cantidad=200"))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenAccountData_WhenTryRemoveAmountHigherThanPossible_ThenBadRequestException() throws Exception {
        mvc.perform(put("/accounts/1/withdraw?cid=4&cantidad=10000")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
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
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), 1000, 2L, null);

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
