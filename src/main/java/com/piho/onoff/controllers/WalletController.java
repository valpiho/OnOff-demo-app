package com.piho.onoff.controllers;

import com.piho.onoff.domain.DTO.EntryDTO;
import com.piho.onoff.domain.DTO.WalletDTO;
import com.piho.onoff.domain.HttpResponse;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.services.EntryService;
import com.piho.onoff.services.ValidationErrorService;
import com.piho.onoff.services.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.piho.onoff.constants.WalletConstants.WALLET_DELETED;

@RestController
@RequestMapping("/api/v1/wallets")
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

    @PostMapping
    public ResponseEntity<?> createWallet(@Valid @RequestBody WalletDTO walletDTO, BindingResult result) {
        ArrayList<String> errorList = validationErrorService.validationErrorService(result);
        if (errorList != null) {
            return response(HttpStatus.BAD_REQUEST, errorList.toString());
        }
        return new ResponseEntity<>(walletService.createWallet(walletDTO), HttpStatus.OK);
    }


    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDTO> getWallet(@PathVariable("walletId") String walletId)
            throws NotFoundException {
        return new ResponseEntity<> (walletService.getWalletByWalletId(walletId), HttpStatus.OK);
    }

    @GetMapping("/{walletId}/entries")
    public ResponseEntity<List<EntryDTO>> getWalletEntriesList(@PathVariable("walletId") String walletId) throws NotFoundException {
        return new ResponseEntity<>(entryService.getEntriesListByWalletId(walletId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<WalletDTO>> getWalletsList() throws NotFoundException {
        return new ResponseEntity<> (walletService.getWalletsList(), HttpStatus.OK);
    }

    @PatchMapping("/{walletId}")
    public ResponseEntity<?> updateWallet(@PathVariable("walletId") String walletId,
                                          @Valid @RequestBody WalletDTO walletDTO, BindingResult result)
            throws NotFoundException {
        ArrayList<String> errorList = validationErrorService.validationErrorService(result);
        if (errorList != null) {
            return response(HttpStatus.BAD_REQUEST, errorList.toString());
        }
        return new ResponseEntity<>(walletService.updateWallet(walletId, walletDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<HttpResponse> deleteWallet(@PathVariable("walletId") String walletId) {
        walletService.deleteWalletByWalletId(walletId);
        return response(HttpStatus.OK, WALLET_DELETED);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, message), httpStatus);
    }
}
