package com.piho.onoff.services;

import com.piho.onoff.domain.Wallet;
import com.piho.onoff.exceptions.domain.UniqueFieldExistException;
import com.piho.onoff.exceptions.domain.NotFoundException;

import java.util.List;

public interface WalletService {

    Wallet createWallet(Wallet wallet) throws UniqueFieldExistException;

    Wallet getWalletByWalletId(String walletId) throws NotFoundException;

    List<Wallet> getWalletsList();

    Wallet updateWallet(String walletId, Wallet wallet) throws NotFoundException;

    void deleteWalletByWalletId(String walletId);
}
