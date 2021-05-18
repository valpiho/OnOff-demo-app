package com.piho.onoff.services;

import com.piho.onoff.domain.DTO.WalletDTO;
import com.piho.onoff.domain.Wallet;
import com.piho.onoff.exceptions.domain.BadRequestException;
import com.piho.onoff.exceptions.domain.NotFoundException;
import com.piho.onoff.repositories.WalletRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.piho.onoff.constants.WalletConstants.NO_WALLET_FOUND_BY_WALLET_ID;
import static com.piho.onoff.constants.WalletConstants.WALLET_TITLE_EXISTS;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final BitFinexService bitFinexService;

    public WalletService(WalletRepository walletRepository,
                         ModelMapper modelMapper,
                         BitFinexService bitFinexService) {
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.bitFinexService = bitFinexService;
    }

    public WalletDTO createWallet(WalletDTO walletDTO) {
        checkIfWalletTitleExists(walletDTO.getTitle());

        Wallet wallet = modelMapper.map(walletDTO, Wallet.class);
        wallet.setWalletId(generateWalletId());
        wallet.setEntries(new ArrayList<>());
        walletRepository.save(wallet);

        return modelMapper.map(wallet, WalletDTO.class);
    }

    public WalletDTO getWalletByWalletId(String walletId) throws NotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        checkIfWalletExists(walletId, wallet);
        wallet.getEntries().forEach(bitFinexService::updateCurrentValueAndProfit);
        return modelMapper.map(wallet, WalletDTO.class);
    }

    public List<WalletDTO> getWalletsList() throws NotFoundException {
        List<Wallet> wallets = walletRepository.findAll();
        if (wallets.size() == 0) {
            throw new NotFoundException("No wallets found");
        }
        return wallets.stream()
                .map(wallet -> modelMapper.map(wallet, WalletDTO.class))
                .collect(Collectors.toList());
    }

    public WalletDTO updateWallet(String walletId, WalletDTO walletDTO) throws NotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        checkIfWalletExists(walletId, wallet);
        if (!walletDTO.getTitle().equals(wallet.getTitle())) {
            checkIfWalletTitleExists(walletDTO.getTitle());
        }
        modelMapper.typeMap(WalletDTO.class, Wallet.class).addMappings(mapper -> mapper.skip(Wallet::setId));
        modelMapper.typeMap(WalletDTO.class, Wallet.class).addMappings(mapper -> mapper.skip(Wallet::setWalletId));
        modelMapper.map(walletDTO, wallet);
        walletRepository.save(wallet);
        return modelMapper.map(wallet, WalletDTO.class);
    }

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

    private void checkIfWalletTitleExists(String title) {
        if (walletRepository.existsByTitle(title)) {
            throw new BadRequestException(WALLET_TITLE_EXISTS);
        }
    }
}
