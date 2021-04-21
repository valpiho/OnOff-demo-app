package com.piho.onoff.domain;

import lombok.*;

@Data
public class BitFinexTicker {

    private double mid;
    private double bid;
    private double ask;
    private double last_price;
    private double low;
    private double high;
    private double volume;
    private double timestamp;
}
