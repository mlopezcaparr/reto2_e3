package com.microcompany.accountsservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @NotBlank(message = "El tipo de cuenta no puede estar vacio")
    private String type;

    @DateTimeFormat
    @Past(message = "la fecha de apertura debe ser en el pasado")
    Date openingDate;

    private int balance;

    @NotNull
    private Long ownerId;

    @Transient
    Customer owner;


}
