package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/accounts", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
@Validated
public interface AccountControllerInterface {

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('DIRECTOR') OR hasAuthority('CASHIER')")
    public ResponseEntity getCuentaById(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") long uid,
                                        @Min(value = 1, message = "Account id must be 1 or higher") @RequestParam long cid);

    @RequestMapping(value = "/{uid}/all", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('DIRECTOR') OR hasAuthority('CASHIER')")
    public ResponseEntity getUserCuentas(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") long uid);

    @RequestMapping(value = "/{uid}/validate", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('DIRECTOR') OR hasAuthority('CASHIER')")
    public ResponseEntity validateCuenta(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") long uid,
                                         @Min(value = 0, message = "The amount of money must be higher 0 or higher")
                                         @RequestParam("cantidad") int cant);

    @PutMapping(value = "/{uid}/add")
    @PreAuthorize("hasAuthority('DIRECTOR')")
    ResponseEntity addToAccount(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") Long uid,
                                @Min(value = 1, message = "Account id must be 1 or higher") @RequestParam(value = "cid") Long cid,
                                @Min(value = 0, message = "The amount of money must be higher 0 or higher")
                                @RequestParam(value = "cantidad", defaultValue = "") int cant);

    @PutMapping(value = "/{uid}/withdraw")
    @PreAuthorize("hasAuthority('DIRECTOR')")
    ResponseEntity removeFromAccount(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") Long uid,
                                     @Min(value = 1, message = "Account id must be 1 or higher") @RequestParam(value = "cid") Long cid,
                                     @Min(value = 0, message = "The amount of money must be higher 0 or higher")
                                     @RequestParam(value = "cantidad", defaultValue = "") int cant);

    @PutMapping(value = "/{cid}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('DIRECTOR')")
    ResponseEntity updateAccount(@Min(value = 1, message = "Account id must be 1 or higher") @PathVariable("cid") Long cid,
                                 @Valid @RequestBody Account account);

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('DIRECTOR')")
    ResponseEntity createAccount(@Valid @RequestBody Account account);

    @DeleteMapping("/{uid}")
    @PreAuthorize("hasAuthority('DIRECTOR')")
    ResponseEntity deleteAccount(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") Long uid,
                                 @Min(value = 1, message = "Account id must be 1 or higher") @RequestParam(value = "cid") Long cid);

    @DeleteMapping("/{uid}/all")
    @PreAuthorize("hasAuthority('DIRECTOR')")
    ResponseEntity deleteUserAccounts(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") Long uid);
}
