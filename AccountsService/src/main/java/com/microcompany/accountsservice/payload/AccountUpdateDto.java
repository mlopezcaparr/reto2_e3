package com.microcompany.accountsservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "Modelo para actualizar cuenta")
public class AccountUpdateDto {
    @NotBlank(message = "Type must be not blank")
    @Schema(description = "Tipo de cuenta", example = "Ahorro")
    private String type;
}
