package com.piho.onoff.controllers;

import com.piho.onoff.domain.Entry;
import com.piho.onoff.domain.HttpResponse;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.exceptions.domain.ServerIsUnderMaintenanceException;
import com.piho.onoff.services.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/entries")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }
// "btc, iot, eth, neo, eos, got, trx, eut\"
    @PostMapping("/add")
    public ResponseEntity<?> createEntry(@RequestParam("name") @Pattern(
                                                regexp="^[a-z]{3}$",
                                                message = "Cryptocurrency name must be 3 characters long") String cryptocurrencyName,
                                         @RequestParam("amount") @Min(0) @Max(5000) double amount,
                                         @RequestParam("walletId") @Pattern(
                                                 regexp="^[0-9]{10}$",
                                                 message = "WalletId must be 10 numbers long") String walletId)
            throws IOException, ServerIsUnderMaintenanceException, NotFoundException {
        Entry entry = entryService.createEntry(cryptocurrencyName, amount, walletId);
        return new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<Entry> getEntry(@PathVariable("entryId") String entryId)
            throws ServerIsUnderMaintenanceException, IOException, NotFoundException {
        Entry entry = entryService.getEntryByEntryId(entryId);
        return new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Entry>> getEntriesList()
            throws IOException, ServerIsUnderMaintenanceException {
        List<Entry> entries = entryService.getEntriesList();
        return new ResponseEntity<> (entries, HttpStatus.OK);
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<HttpResponse> deleteEntry(@PathVariable("entryId") String entryId)
            throws NotFoundException {
        entryService.deleteEntryByEntryId(entryId);
        return response(HttpStatus.OK, "Entry was deleted successfully");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, message), httpStatus);
    }
}
