package com.microcompany.accountsservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description ="Modelo de respuesta del propietario de la cuenta")
public class CustomerDto {
    @Schema(description ="Nombre del propietario", example = "Juan Garcia")
    private String name;
    @Schema(description ="Email del propietario", example = "juangarcia@mail.com")
    private String email;
}
