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
}
