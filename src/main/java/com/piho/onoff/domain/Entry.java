package com.piho.onoff.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @NotNull
    @Column(nullable = false, updatable = false)
    private String entryId;

    private String cryptocurrencyName;
    private double amount;
    private double purchasedValue;
    private double currentValue;
    private double currentProfit;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(
            name = "wallet_id", referencedColumnName = "id",
            nullable = false, updatable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "walletId")
    @JsonIdentityReference(alwaysAsId=true)
    private Wallet wallet;
}
