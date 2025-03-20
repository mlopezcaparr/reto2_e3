package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuentas")
public interface AccountControllerInterface {


    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public ResponseEntity getCuentaById(@PathVariable("uid") long uid, @RequestParam long cid);

    @RequestMapping(value = "/{uid}/all", method = RequestMethod.GET)
    public ResponseEntity getUserCuentas(@PathVariable("uid") long uid);

    @RequestMapping(value = "/{uid}/validate", method = RequestMethod.GET)
    public ResponseEntity validateCuenta(@PathVariable("uid") long uid ,@RequestParam("cantidad") int cant);
}
