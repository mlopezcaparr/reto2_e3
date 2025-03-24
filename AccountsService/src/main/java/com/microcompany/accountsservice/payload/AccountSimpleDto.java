package com.microcompany.accountsservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@Schema(description ="Modelo de respuesta de cuenta simplificada")
public class AccountSimpleDto {

    @Schema(description = "Identificador de la cuenta", example = "1")
    private Long id;

    @NotBlank(message = "Account must be not blank")
    @Schema(description ="Tipo de cuenta", example = "Ahorro")
    private String type;

    @DateTimeFormat
    @Past(message = "The Date of the account creation must be lower than current time")
    @Schema(description = "Fecha de apertura de la cuenta")
    Date openingDate;

    @Min(value = 0, message = "Balance must be 0 or higher")
    @NotNull(message = "Balance must be not null")
    @Schema(description ="Cantidad de la cuenta", example = "1000")
    private int balance;

    @Min(value = 1, message = "Owner id must be 1 or higher")
    @NotNull(message = "Owner id must be not null")
    @Schema(description ="Identicador del propietario de la cuenta", example = "1")
    private Long ownerId;

}
