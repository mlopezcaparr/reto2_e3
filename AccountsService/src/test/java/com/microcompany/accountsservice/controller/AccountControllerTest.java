package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.exception.GlobalException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.payload.ApiResponse;
import com.microcompany.accountsservice.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest /**Todos los contextos cargados**/
class AccountControllerTest {

    @Autowired
    private AccountController controller;

    @Autowired
    private AccountService service;

    @Test
        void givenAccountData_WhenTryAddAmount_ThenSuccess() {
        Account account = service.getAccount(1L);
        ResponseEntity response = controller.addToAccount(1L, 1L,2000);
        Account addedToAccount = service.getAccount(1L);
        assertThat(HttpStatus.NO_CONTENT.value(), equalTo(response.getStatusCode().value()));
        assertThat(addedToAccount.getBalance(), greaterThan(account.getBalance()));
    }

    @Test
    void givenAccountData_WhenTryAddAmount_ThenExceptionNotAccountFound() {
        assertThatExceptionOfType(AccountNotfoundException.class).isThrownBy(() -> {
            ResponseEntity response = controller.addToAccount(1L, 80L,2000);
            assertThat(HttpStatus.NOT_FOUND.value(), equalTo(response.getStatusCode().value()));
        });
    }

    @Test
    void givenAccountData_WhenTryRemoveAmount_ThenSuccess() {
        Account account = service.getAccount(1L);
        ResponseEntity response = controller.removeFromAccount(1L, 1L,200);
        Account removedToAccount = service.getAccount(1L);
        assertThat(HttpStatus.NO_CONTENT.value(), equalTo(response.getStatusCode().value()));
        assertThat(removedToAccount.getBalance(), lessThan(account.getBalance()));
    }

    @Test
    void givenAccountData_WhenTryRemoveAmountHigherThanPossible_ThenBadRequestException() {
        assertThatExceptionOfType(GlobalException.class).isThrownBy(() -> {
            ResponseEntity response = controller.addToAccount(1L, 80L,23000);
            assertThat(HttpStatus.PRECONDITION_FAILED.value(), equalTo(response.getStatusCode().value()));
        });
    }

    @Test
    void givenNewTypeForAccount_WhenTryToUpdateAccount_ThenSuccess() {
        Account beforeUpdateAccount = service.getAccount(1L);
        Account afterUpdateAccount = new Account(null, "Personal", Date.from(LocalDate.parse("2000-10-01")
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), 100, 1L, null);
        ResponseEntity response = controller.updateAccount(1L, afterUpdateAccount);
        afterUpdateAccount = service.getAccount(1L);
        assertThat(HttpStatus.NO_CONTENT.value(), equalTo(response.getStatusCode().value()));
        assertThat(afterUpdateAccount.getType(), equalTo("Personal"));
        assertThat(afterUpdateAccount.getType(), is(not(beforeUpdateAccount.getType())));

    }

    @Test
    void givenNewTypeForAccount_WhenTryToUpdateToInvalidAccountType_ThenAccountTypeExceptionConstrain() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            ResponseEntity response = controller.updateAccount(1L, new Account(null, "aa", Date.from(LocalDate
                    .parse("2000-10-01").atStartOfDay(ZoneId.systemDefault()).toInstant()), 100, 1L, null));
            assertThat(HttpStatus.PRECONDITION_FAILED.value(), equalTo(response.getStatusCode().value()));
        });
    }

    @Test
    void givenNewAccount_WhenTryToCreateIt_ThenSuccess() {
        Account newAccount = new Account(null, "Personal", Date.from(LocalDate.parse("2004-07-08")
                .atStartOfDay(ZoneId.systemDefault()).toInstant()), 1000, 2L, null);
        ResponseEntity response = controller.createAccount(newAccount);
        assertThat(HttpStatus.CREATED.value(), equalTo(response.getStatusCode().value()));
        List<Account> userAccounts = service.getAccountByOwnerId(2L);
        assertThat(userAccounts.stream().anyMatch(account ->
            account.getType().equals(newAccount.getType()) &&
            account.getBalance() == newAccount.getBalance() &&
            account.getOwnerId().equals(newAccount.getOwnerId())
        ), is(true));
    }

    @Test
    void givenNewAccount_WhenTryToCreateItWithWrongBalance_ThenFailedToCreate() {
        ResponseEntity response = controller.createAccount(new Account(null, "Company", Date.from(LocalDate
                .parse("2000-10-01").atStartOfDay(ZoneId.systemDefault()).toInstant()), 99000, 2L, null));
        assertThat(HttpStatus.BAD_REQUEST.value(), equalTo(response.getStatusCode().value()));
        ApiResponse apiResponse = (ApiResponse) response.getBody();
        assertThat(apiResponse, notNullValue());
        assertThat(apiResponse.getMessage(), equalTo("User 2 cannot create account with balance 99000"));
    }
}
