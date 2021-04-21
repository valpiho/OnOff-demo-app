package com.piho.onoff.services;

import com.piho.onoff.domain.Entry;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.exceptions.domain.ServerIsUnderMaintenanceException;

import java.io.IOException;
import java.util.List;

public interface EntryService {

    Entry createEntry(String cryptocurrencyName, double amount, String walletId)
            throws IOException, ServerIsUnderMaintenanceException, NotFoundException;

    Entry getEntryByEntryId(String entryId)
            throws IOException, ServerIsUnderMaintenanceException, NotFoundException;

    List<Entry> getEntriesList()
            throws IOException, ServerIsUnderMaintenanceException;

    List<Entry> getEntriesListByWalletId(String walletId);

    void deleteEntryByEntryId(String entryId)
            throws NotFoundException;


}
