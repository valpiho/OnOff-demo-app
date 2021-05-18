package com.piho.onoff.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_title", columnNames = "title")})
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String walletId;

    private String title;
    private String fullName;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Entry> entries;

    public Wallet() {
    }

    public Wallet(Long id, String walletId, String title, String fullName, String email, List<Entry> entries) {
        this.id = id;
        this.walletId = walletId;
        this.title = title;
        this.fullName = fullName;
        this.email = email;
        this.entries = entries;
    }
}
