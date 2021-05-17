package com.piho.onoff.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Column(nullable = false, updatable = false)
    private String entryId;

    private String cryptocurrencyName;
    private double amount;
    private double purchasedValue;
    private double currentValue;
    private double currentProfit;
    private Date createdAt;

    @ManyToOne
    @JoinColumn(
            name = "wallet_id", referencedColumnName = "id",
            nullable = false, updatable = false)
    private Wallet wallet;
}
