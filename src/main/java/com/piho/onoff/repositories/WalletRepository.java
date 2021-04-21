package com.piho.onoff.repositories;

import com.piho.onoff.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findWalletByWalletId(String walletId);

    Wallet findWalletByTitle(String title);
}
