package com.piho.onoff.domain.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
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
