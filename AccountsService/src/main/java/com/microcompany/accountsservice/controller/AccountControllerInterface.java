package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/accounts")
@Validated
@Tag(name = "Endpoints Para API de Accounts", description = "Endpoints para la gestión de cuentas de los usuarios")
public interface AccountControllerInterface {


    @Operation(summary = "Método pedir una cuenta del usuario", description = "Método para pedir una cuenta de un cliente" +
            " pasando el id del cliente y el id de la cuenta, devuelve un JSON o XML.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Si existe, devolverá como resultado la cuenta."),
            @ApiResponse(responseCode = "404", description = "Si no existen, devolverá un mensaje de que no la encontrado."),
            @ApiResponse(responseCode = "412", description = "Devolvera este error en caso de pasarle un parametro erróneo."),
    })
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getCuentaById(@PathVariable("uid") long uid, @RequestParam long cid);

    @Operation(summary = "Método para pedir cuentas de un usuario", description = "Método que solicita las cuentas" +
            " de un cliente pasando el id del cliente y delvuelve una lista de cuentas, devuelve un JSON o XML.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Si existe, devolverá como resultado la cuenta/las cuentas."),
            @ApiResponse(responseCode = "404", description = "Si no existen, devolverá un mensaje de que no la encontrado."),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @RequestMapping(value = "/{uid}/all", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getUserCuentas(@PathVariable("uid") long uid);

    @Operation(summary = "Método para validar préstamo de un usuario", description = "Método que valida si un usuario" +
            " puede extraer 'x' cantidad de dinero de sus cuentas, devuelve un JSON o XML.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Si es valido, devolverá como resultado un mensaje de true."),
            @ApiResponse(responseCode = "404", description = "Si no es valido, devolverá como resultado un mensaje de false."),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @RequestMapping(value = "/{uid}/validate", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity validateCuenta(@PathVariable("uid") long uid ,@RequestParam("cantidad") int cant);

    @Operation(summary = "Método para añadir a una cuenta", description = "Método que permite a un usuario agregar dinero" +
            " a una cuenta, si el método se ha ejecutado correctamente no devolverá contenido, si fallá devuelve" +
            " un JSON o XML con los errores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se ha agregado la cantidad correctamente sin errores."),
            @ApiResponse(responseCode = "404", description = "Hubo un problema, no se puedo encontrar la cuenta o el usuario"),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @PutMapping(value = "/{uid}/add", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity addToAccount(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") Long uid, @Min(1) @RequestParam(value = "cid") Long cid,
                                @Min(0) @RequestParam(value = "cantidad", defaultValue = "") int cant);

    @Operation(summary = "Método para extraer de cuentas", description = "Método que permite a un usuario extraer dinero" +
            " de sus cuentas, si el método se ha ejecutado correctamente no devolverá contenido, si fallá devuelve" +
            " un JSON o XML con los errores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se ha agregado la cantidad correctamente sin errores."),
            @ApiResponse(responseCode = "400", description = "No se pudo extraer la cantidad porque superá el montón permitido"),
            @ApiResponse(responseCode = "404", description = "Hubo un problema, no se puedo encontrar la cuenta o el usuario"),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @PutMapping(value = "/{uid}/withdraw", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity removeFromAccount(@Min(value = 1, message = "Owner id must be 1 or higher") @PathVariable("uid") Long uid, @Min(1) @RequestParam(value = "cid") Long cid,
                                @Min(0) @RequestParam(value = "cantidad", defaultValue = "") int cant);

    @Operation(summary = "Método para actualizar cuentas", description = "Método que permite a actualizar la cuenta de" +
            " un usuario pasando el id de dicha cuenta por parametros, si el método se ha ejecutado correctamente no" +
            " devolverá contenido, si fallá devuelve un JSON o XML con los errores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se ha agregado la cantidad correctamente sin errores."),
            @ApiResponse(responseCode = "400", description = "La cuenta se paso como parametro de JSON vacío."),
            @ApiResponse(responseCode = "404", description = "Hubo un problema, no se puedo encontrar la cuenta asociada al id"),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @PutMapping(value = "/{cid}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity updateAccount(@Min(1) @PathVariable("cid") Long cid, @Valid @RequestBody Account account);

    @Operation(summary = "Método para crear cuenta", description = "Método que permite crear una cuenta para" +
            " un usuario pasando el id de dicha cuenta por parametros, si el método se ha ejecutado correctamente no" +
            " devolverá contenido, si fallá devuelve un JSON o XML con los errores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se creado la cuenta correctamente sin errores."),
            @ApiResponse(responseCode = "400", description = "Devolverá este error si no puede crear una cuenta con el balance"),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @PostMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity createAccount(@Valid @RequestBody Account account);

    @Operation(summary = "Método para borrar cuenta", description = "Método que permite borrar una cuenta para" +
            " un usuario pasando el id de dicha cuenta y el del usuario por parametros, si el método se ha ejecutado " +
            " correctamente no devolverá contenido, si fallá devuelve un JSON o XML con los errores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se borra la cuenta correctamente sin errores."),
            @ApiResponse(responseCode = "400", description = "Devolverá este error si el usuario no es propietario."),
            @ApiResponse(responseCode = "404", description = "Devolverá este error si no puedo encontrar una cuenta."),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @DeleteMapping("/{uid}")
    ResponseEntity deleteAccount(@PathVariable("uid") Long uid, @RequestParam(value = "cid") Long cid);

    @Operation(summary = "Método para borrar cuentas de usuario", description = "Método que permite borrar las cuentas de" +
            " un usuario pasando el id del usuario por parametros, si el método se ha ejecutado " +
            " correctamente no devolverá contenido, si fallá devuelve un JSON o XML con los errores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se borrán la cuenta correctamente sin errores."),
            @ApiResponse(responseCode = "404", description = "Devolverá este error si no encontró ninguna cuenta asociada al usuario."),
            @ApiResponse(responseCode = "412", description = "Devolverá este error en caso de pasarle un parametro erróneo."),
    })
    @DeleteMapping("/{uid}/all")
    ResponseEntity deleteUserAccounts(@PathVariable("uid") Long uid);
}
