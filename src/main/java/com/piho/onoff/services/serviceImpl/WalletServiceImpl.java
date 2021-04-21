package com.piho.onoff.services.serviceImpl;

import com.piho.onoff.domain.Wallet;
import com.piho.onoff.exceptions.domain.UniqueFieldExistException;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.services.WalletService;
import com.piho.onoff.repositories.WalletRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.piho.onoff.constants.WalletImplConstants.NO_WALLET_FOUND_BY_WALLET_ID;
import static com.piho.onoff.constants.WalletImplConstants.WALLET_TITLE_EXISTS;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public Wallet createWallet(Wallet wallet) throws UniqueFieldExistException {
        Wallet newWallet = new Wallet();

        newWallet.setWalletId(generateWalletId());
        newWallet.setTitle(wallet.getTitle());
        newWallet.setFullName(wallet.getFullName());
        newWallet.setEmail(wallet.getEmail());
        newWallet.setFunds(0.0);
        newWallet.setBtcAmount(0.0);
        newWallet.setCurrentBtcValue(0.0);

        try {
            walletRepository.save(newWallet);
        } catch (DataIntegrityViolationException exception) {
            if (walletRepository.findWalletByTitle(newWallet.getTitle()) != null) {
                throw new UniqueFieldExistException(WALLET_TITLE_EXISTS);
            }
            throw exception;
        }
        return newWallet;
    }

    @Override
    public Wallet getWalletByWalletId(String walletId) throws NotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        checkIfWalletExists(walletId, wallet);
        return wallet;
    }

    @Override
    public List<Wallet> getWalletsList() {
        return walletRepository.findAll();
    }

    @Override
    public Wallet updateWallet(String walletId, Wallet wallet) throws NotFoundException {
        Wallet updatedWallet = walletRepository.findWalletByWalletId(walletId);
        checkIfWalletExists(walletId, updatedWallet);
        updatedWallet.setFullName(wallet.getFullName());
        updatedWallet.setEmail(wallet.getEmail());
        walletRepository.save(updatedWallet);
        return updatedWallet;
    }

    @Override
    public void deleteWalletByWalletId(String walletId) {
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        walletRepository.delete(wallet);
    }

    private String generateWalletId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private void checkIfWalletExists(String walletId, Wallet wallet) throws NotFoundException {
        if (wallet == null) {
            throw new NotFoundException(NO_WALLET_FOUND_BY_WALLET_ID + walletId);
        }
    }
}
