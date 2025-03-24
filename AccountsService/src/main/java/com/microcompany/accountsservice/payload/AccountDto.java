package com.microcompany.accountsservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description ="Modelo de respuesta del detalle de la cuenta")
public class AccountDto {
    @Schema(description = "Identificador de la cuenta", example = "1")
    private Long id;
    @Schema(description ="Tipo de cuenta", example = "Ahorro")
    private String type;
    @Schema(description = "Fecha de apertura de la cuenta")
    Date openingDate;
    @Schema(description ="Cantidad de la cuenta", example = "1000")
    private int balance;
    private CustomerDto owner;
}
