package com.piho.onoff.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_title", columnNames = "title")})
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String walletId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email
    private String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double funds;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double btcAmount;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double currentBtcValue;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "entryId")
    @JsonIdentityReference(alwaysAsId=true)
    private List<Entry> entries;
}
