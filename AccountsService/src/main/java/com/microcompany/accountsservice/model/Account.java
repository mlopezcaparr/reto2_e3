package com.microcompany.accountsservice.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
    private Long id;

    @NotBlank(message = "Account must be not blank")
    private String type;

    @DateTimeFormat
    @Past(message = "The Date of the account creation must be lower than current time")
    Date openingDate;

    @Min(value = 0, message = "Balance must be 0 or higher")
    @NotNull(message = "Balance must be not null")
    private int balance;

    @Min(value = 1, message = "Owner id must be 1 or higher")
    @NotNull(message = "Owner id must be not null")
    private Long ownerId;

    @Transient
    Customer owner;


}
