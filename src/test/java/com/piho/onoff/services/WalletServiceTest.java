package com.piho.onoff.services;

import com.piho.onoff.domain.DTO.WalletDTO;
import com.piho.onoff.domain.Wallet;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.repositories.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class WalletServiceTest {

    @Autowired
    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    void createWallet() {
        WalletDTO walletDTO = new WalletDTO("YourWallet", "Aleksander Shown", "alex.shown@gmail.com");
        Wallet wallet = new Wallet(1L, "3231231231", "MyWallet", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());

        doReturn(wallet).when(walletRepository).save(any());

        WalletDTO returnedWalletDTO = walletService.createWallet(walletDTO);

        Assertions.assertNotNull(returnedWalletDTO, "The saved widget should not be null");
        Assertions.assertEquals("YourWallet", returnedWalletDTO.getTitle());

    }

    @Test
    void getWalletByWalletId() throws NotFoundException {
        WalletDTO walletDTO = new WalletDTO("MyWallet", "Valentin Piho", "val.piho@gmail.com");
        Wallet wallet = new Wallet(1L, "3231231231", "MyWallet", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());

        doReturn(wallet).when(walletRepository).findWalletByWalletId("3231231231");

        WalletDTO returnedWalletDTO = walletService.getWalletByWalletId("3231231231");

        assertNotEquals(returnedWalletDTO, walletDTO, "Wallet should not be found");
    }

    @Test
    void updateWallet() throws NotFoundException {
        WalletDTO updatedWalletDTO = new WalletDTO("MyWallet2", "Valentin", "piho@gmail.com");
        Wallet wallet = new Wallet(1L, "3231312231", "MyWallet", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());
        Wallet updatedWallet = new Wallet(1L, "3231312231", "MyWallet2", "Valentin", "piho@gmail.com", new ArrayList<>());

        doReturn(wallet).when(walletRepository).findWalletByWalletId("3231312231");
        doReturn(updatedWallet).when(walletRepository).save(wallet);

        WalletDTO returnedWalletDTO = walletService.updateWallet("3231312231", updatedWalletDTO);

        Assertions.assertEquals("MyWallet2", returnedWalletDTO.getTitle(), "Title should be updated");
        Assertions.assertEquals("Valentin", returnedWalletDTO.getFullName(), "Full name should be updated");
        Assertions.assertEquals("piho@gmail.com", returnedWalletDTO.getEmail(), "Email should be updated");
    }
}
