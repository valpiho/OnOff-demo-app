package com.piho.onoff.services.serviceImpl;

import com.piho.onoff.domain.Entry;
import com.piho.onoff.domain.BitFinexTicker;
import com.piho.onoff.domain.Wallet;
import com.piho.onoff.repositories.WalletRepository;
import com.piho.onoff.repositories.EntryRepository;
import com.piho.onoff.services.EntryService;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.exceptions.domain.ServerIsUnderMaintenanceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.piho.onoff.constants.EntryImplConstants.*;
import static com.piho.onoff.constants.WalletImplConstants.NO_WALLET_FOUND_BY_WALLET_ID;

@Service
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final WalletRepository walletRepository;

    public EntryServiceImpl(EntryRepository entryRepository,
                            WalletRepository walletRepository) {
        this.entryRepository = entryRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public Entry createEntry(String cryptocurrencyName, double amount, String walletId)
            throws ServerIsUnderMaintenanceException, IOException, NotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        checkIfWalletExists(walletId, wallet);

        BitFinexTicker ticker = getBitFinexTicker(cryptocurrencyName);

        Entry entry = new Entry();
        entry.setEntryId(generateEntryId());
        entry.setCryptocurrencyName(cryptocurrencyName);
        entry.setAmount(amount);
        entry.setPurchasedValue(amount * ticker.getAsk());
        entry.setCurrentValue(amount * ticker.getBid());
        entry.setWallet(walletRepository.findWalletByWalletId(walletId));
        entry.setCreatedAt(new Date());

        entryRepository.save(entry);
        return entry;
    }

    @Override
    public Entry getEntryByEntryId(String entryId) throws NotFoundException, ServerIsUnderMaintenanceException, IOException {

        Entry entry = entryRepository.findEntryByEntryId(entryId);
        checkIfEntryExists(entryId, entry);

        BitFinexTicker ticker = getBitFinexTicker(entry.getCryptocurrencyName());
        entry.setCurrentValue(entry.getAmount() * ticker.getBid());
        return entry;
    }

    @Override
    public List<Entry> getEntriesList() {
        List<Entry> entries = entryRepository.findAll();
        updateCurrentValueAndProfit(entries);
        return entries;
    }

    @Override
    public List<Entry> getEntriesListByWalletId(String walletId) {
        List<Entry> entries = entryRepository.findAllEntriesByWallet(walletId);
        updateCurrentValueAndProfit(entries);
        return entries;
    }

    @Override
    public void deleteEntryByEntryId(String entryId) throws NotFoundException {
        Entry entry = entryRepository.findEntryByEntryId(entryId);
        checkIfEntryExists(entryId, entry);
        entryRepository.delete(entry);
    }

    private String generateEntryId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private void checkIfEntryExists(String entryId, Entry entry) throws NotFoundException {
        if (entry == null) {
            throw new NotFoundException(NO_ENTRY_FOUND_BY_ENTRY_ID + entryId);
        }
    }

    private void checkIfWalletExists(String walletId, Wallet wallet) throws NotFoundException {
        if (wallet == null) {
            throw new NotFoundException(NO_WALLET_FOUND_BY_WALLET_ID + walletId);
        }
    }

    private BitFinexTicker getBitFinexTicker(String cryptocurrencyName) throws IOException, ServerIsUnderMaintenanceException, NotFoundException {
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

    private void updateCurrentValueAndProfit(List<Entry> entries) {
        entries.forEach(entry -> {
            try {
                entry.setCurrentValue(entry.getAmount() * getBitFinexTicker(entry.getCryptocurrencyName()).getBid());
                entry.setCurrentProfit(entry.getCurrentValue() - entry.getPurchasedValue());
            } catch (IOException | ServerIsUnderMaintenanceException | NotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }
}
