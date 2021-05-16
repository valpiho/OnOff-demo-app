package com.piho.onoff.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "entryId")
    @JsonIdentityReference(alwaysAsId=true)
    private List<Entry> entries;
}
