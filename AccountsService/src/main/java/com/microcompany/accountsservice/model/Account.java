package com.microcompany.accountsservice.model;

import javax.persistence.*;
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

    private String type;

    @Column(name = "openingDate")
    @DateTimeFormat
    Date openingDate;

    private int balance;

    @Column(name = "ownerId")
    private Long ownerId;

    @Transient
    Customer owner;


}
