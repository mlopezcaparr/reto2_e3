package com.microcompany.accountsservice.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse {

    private String message;
    private boolean success;
}
