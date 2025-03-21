package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/accounts")
public interface AccountControllerInterface {


    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public ResponseEntity getCuentaById(@PathVariable("uid") long uid, @RequestParam long cid);

    @RequestMapping(value = "/{uid}/all", method = RequestMethod.GET)
    public ResponseEntity getUserCuentas(@PathVariable("uid") long uid);

    @RequestMapping(value = "/{uid}/validate", method = RequestMethod.GET)
    public ResponseEntity validateCuenta(@PathVariable("uid") long uid ,@RequestParam("cantidad") int cant);

    @PutMapping("/{ownerId}/add")
    ResponseEntity addToAccount(@Min(1) @PathVariable("ownerId") Long cid, @Min(1) @RequestParam(value = "cuentaid") Long aid,
                                @Min(0) @RequestParam(value = "cantidad", defaultValue = "") int cantidad);

    @PutMapping("/{ownerId}/withdraw")
    ResponseEntity removeFromAccount(@Min(1) @PathVariable("ownerId") Long cid, @Min(1) @RequestParam(value = "cuentaid") Long aid,
                                @Min(0) @RequestParam(value = "cantidad", defaultValue = "") int cantidad);

    @PutMapping(value = "/{cuentaId}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity updateAccount(@Min(1) @PathVariable("cuentaId") Long aid, @Valid @RequestBody Account account);

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity createAccount(@Valid @RequestBody Account account);

    @DeleteMapping("/{uid}")
    ResponseEntity deleteAccount(@PathVariable("uid") Long uid, @RequestParam(value = "cuentaId") Long cuentaId);

    @DeleteMapping("/{uid}/all")
    ResponseEntity deleteUserAccounts(@PathVariable("uid") Long uid);
}
