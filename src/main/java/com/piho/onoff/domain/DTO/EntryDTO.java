package com.piho.onoff.domain.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EntryDTO {

    private String entryId;
    private String cryptocurrencyName;
    private double amount;
    private double purchasedValue;
    private double currentValue;
    private double currentProfit;
    private String walletId;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
}
