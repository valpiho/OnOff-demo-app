package com.piho.onoff.services;

import com.piho.onoff.domain.DTO.EntryDTO;
import com.piho.onoff.domain.Entry;
import com.piho.onoff.domain.BitFinexTicker;
import com.piho.onoff.domain.Wallet;
import com.piho.onoff.repositories.WalletRepository;
import com.piho.onoff.repositories.EntryRepository;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.exceptions.domain.ServerIsUnderMaintenanceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.piho.onoff.constants.EntryConstants.*;
import static com.piho.onoff.constants.WalletConstants.NO_WALLET_FOUND_BY_WALLET_ID;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    private final WalletRepository walletRepository;
    private final BitFinexService bitFinexService;
    private final ModelMapper modelMapper;

    public EntryService(EntryRepository entryRepository,
                        WalletRepository walletRepository,
                        BitFinexService bitFinexService,
                        ModelMapper modelMapper) {
        this.entryRepository = entryRepository;
        this.walletRepository = walletRepository;
        this.bitFinexService = bitFinexService;
        this.modelMapper = modelMapper;
    }

    public EntryDTO createEntry(String cryptocurrencyName, double amount, String walletId)
            throws ServerIsUnderMaintenanceException, IOException, NotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        checkIfWalletExists(walletId, wallet);

        BitFinexTicker ticker = bitFinexService.getBitFinexTicker(cryptocurrencyName);

        Entry entry = new Entry();
        entry.setEntryId(generateEntryId());
        entry.setCryptocurrencyName(cryptocurrencyName);
        entry.setAmount(amount);
        entry.setPurchasedValue(amount * ticker.getAsk());
        entry.setCurrentValue(amount * ticker.getBid());
        entry.setWallet(walletRepository.findWalletByWalletId(walletId));
        entry.setCreatedAt(new Date());

        entryRepository.save(entry);
        bitFinexService.updateCurrentValueAndProfit(entry);
        return modelMapper.map(entry, EntryDTO.class);
    }

    public EntryDTO getEntryByEntryId(String entryId) throws NotFoundException, ServerIsUnderMaintenanceException, IOException {

        Entry entry = entryRepository.findEntryByEntryId(entryId);
        checkIfEntryExists(entryId, entry);

        BitFinexTicker ticker = bitFinexService.getBitFinexTicker(entry.getCryptocurrencyName());
        entry.setCurrentValue(entry.getAmount() * ticker.getBid());
        return modelMapper.map(entry, EntryDTO.class);
    }

    public List<EntryDTO> getEntriesList() {
        List<Entry> entries = entryRepository.findAll();
        entries.forEach(bitFinexService::updateCurrentValueAndProfit);
        return entries.stream()
                .map(entry -> modelMapper.map(entry, EntryDTO.class))
                .collect(Collectors.toList());
    }

    public List<EntryDTO> getEntriesListByWalletId(String walletId) throws NotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        checkIfWalletExists(walletId, wallet);
        List<Entry> entries = wallet.getEntries();
        entries.forEach(bitFinexService::updateCurrentValueAndProfit);
        return entries.stream()
                .map(entry -> modelMapper.map(entry, EntryDTO.class))
                .collect(Collectors.toList());
    }

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
}
