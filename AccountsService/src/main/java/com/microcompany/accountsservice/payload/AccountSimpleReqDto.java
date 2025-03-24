package com.microcompany.accountsservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Modelo para crear una cuenta")
public class AccountSimpleReqDto {
    @NotBlank(message = "Account must be not blank")
    @Schema(description ="Tipo de cuenta", example = "Ahorro")
    private String type;

    @Min(value = 0, message = "Balance must be 0 or higher")
    @NotNull(message = "Balance must be not null")
    @Schema(description ="Cantidad de la cuenta", example = "1000")
    private int balance;

    @Min(value = 1, message = "Owner id must be 1 or higher")
    @NotNull(message = "Owner id must be not null")
    @Schema(description ="Identicador del propietario de la cuenta", example = "1")
    private Long ownerId;
}
