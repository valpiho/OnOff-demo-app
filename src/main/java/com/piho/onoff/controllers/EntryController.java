package com.piho.onoff.controllers;

import com.piho.onoff.domain.DTO.EntryDTO;
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

import static com.piho.onoff.constants.EntryConstants.ENTRY_DELETED;

@RestController
@Validated
@RequestMapping("/api/entries")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }
    @PostMapping("/add")
    public ResponseEntity<?> createEntry(@RequestParam("name") @Pattern(
                                                regexp="^[a-z]{3}$",
                                                message = "Cryptocurrency name must be 3 characters long") String cryptocurrencyName,
                                         @RequestParam("amount") @Min(0) @Max(5000) double amount,
                                         @RequestParam("walletId") @Pattern(
                                                 regexp="^[0-9]{10}$",
                                                 message = "WalletId must be 10 numbers long") String walletId)
            throws IOException, ServerIsUnderMaintenanceException, NotFoundException {
        return new ResponseEntity<>(entryService.createEntry(cryptocurrencyName, amount, walletId), HttpStatus.OK);
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<EntryDTO> getEntry(@PathVariable("entryId") String entryId)
            throws ServerIsUnderMaintenanceException, IOException, NotFoundException {
        return new ResponseEntity<>(entryService.getEntryByEntryId(entryId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EntryDTO>> getEntriesList() {
        return new ResponseEntity<> (entryService.getEntriesList(), HttpStatus.OK);
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<HttpResponse> deleteEntry(@PathVariable("entryId") String entryId)
            throws NotFoundException {
        entryService.deleteEntryByEntryId(entryId);
        return response(HttpStatus.OK, ENTRY_DELETED);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, message), httpStatus);
    }
}
