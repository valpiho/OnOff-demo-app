package com.piho.onoff.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piho.onoff.domain.BitFinexTicker;
import com.piho.onoff.domain.Entry;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.exceptions.domain.ServerIsUnderMaintenanceException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

import static com.piho.onoff.constants.EntryConstants.BITFINIX_IS_UNDER_MAINTENANCE;
import static com.piho.onoff.constants.EntryConstants.NO_CRYPTOCURRENCY_FOUND;

@Service
public class BitFinexService {

    public void updateCurrentValueAndProfit(Entry entry) {
        try {
            entry.setCurrentValue(entry.getAmount() * getBitFinexTicker(entry.getCryptocurrencyName()).getBid());
        } catch (IOException | ServerIsUnderMaintenanceException | NotFoundException exception) {
            exception.printStackTrace();
        }
        entry.setCurrentProfit(entry.getCurrentValue() - entry.getPurchasedValue());
    }

    public BitFinexTicker getBitFinexTicker(String cryptocurrencyName) throws IOException, ServerIsUnderMaintenanceException, NotFoundException {
        bitFinexServerCheck();
        try {
            URL jsonUrl = new URL("https://api.bitfinex.com/v1/pubticker/" + cryptocurrencyName + "eur");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonUrl, BitFinexTicker.class);
        } catch (IOException exception) {
            throw new NotFoundException(NO_CRYPTOCURRENCY_FOUND);
        }
    }

    private void bitFinexServerCheck() throws IOException, ServerIsUnderMaintenanceException {
        URL jsonUrl = new URL("https://api-pub.bitfinex.com/v2/platform/status");
        byte[] status = new byte[3];
        jsonUrl.openStream().read(status);
        if (status[1] != 49) {
            throw new ServerIsUnderMaintenanceException(BITFINIX_IS_UNDER_MAINTENANCE);
        }
    }
}
