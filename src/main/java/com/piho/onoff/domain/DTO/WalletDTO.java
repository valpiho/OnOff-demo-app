package com.piho.onoff.domain.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class WalletDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String walletId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email
    private String email;

    private List<EntryDTO> entries;
}
