package com.piho.onoff.controllers;

import com.piho.onoff.domain.Wallet;
import com.piho.onoff.domain.Entry;
import com.piho.onoff.domain.HttpResponse;
import com.piho.onoff.exceptions.domain.UniqueFieldExistException;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.services.ValidationErrorService;
import com.piho.onoff.services.EntryService;
import com.piho.onoff.services.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final EntryService entryService;
    private final ValidationErrorService validationErrorService;

    public WalletController(WalletService walletService,
                            EntryService entryService,
                            ValidationErrorService validationErrorService) {
        this.walletService = walletService;
        this.entryService = entryService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWallet(@Valid @RequestBody Wallet wallet, BindingResult result)
            throws UniqueFieldExistException {
        ArrayList<String> errorList = validationErrorService.validationErrorService(result);
        if (errorList != null) {
            return response(HttpStatus.BAD_REQUEST, errorList.toString());
        }
        Wallet newWallet = walletService.createWallet(wallet);
        return new ResponseEntity<>(newWallet, HttpStatus.OK);
    }


    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable("walletId") String walletId)
            throws NotFoundException {
        Wallet wallet = walletService.getWalletByWalletId(walletId);
        return new ResponseEntity<> (wallet, HttpStatus.OK);
    }

    @GetMapping("/{walletId}/entries")
    public ResponseEntity<List<Entry>> getWalletEntriesList(@PathVariable("walletId") String walletId) {
        List<Entry> entries = entryService.getEntriesListByWalletId(walletId);
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Wallet>> getWalletsList() {
        List<Wallet> wallets = walletService.getWalletsList();
        return new ResponseEntity<> (wallets, HttpStatus.OK);
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<?> updateWallet(@Valid @PathVariable("walletId") String walletId,
                                          @RequestBody Wallet wallet, BindingResult result)
            throws NotFoundException {
        ArrayList<String> errorList = validationErrorService.validationErrorService(result);
        if (errorList != null) {
            return response(HttpStatus.BAD_REQUEST, errorList.toString());
        }
        Wallet updatedWallet = walletService.updateWallet(walletId, wallet);
        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<HttpResponse> deleteWallet(@PathVariable("walletId") String walletId) {
        walletService.deleteWalletByWalletId(walletId);
        return response(HttpStatus.OK, "Wallet was deleted successfully");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, message), httpStatus);
    }
}
