package com.piho.onoff.domain.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
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

    public WalletDTO() {};

    public WalletDTO(String walletId,
                     @NotBlank(message = "Title is required") String title,
                     @NotBlank(message = "Full name is required") String fullName,
                     @Email String email,
                     List<EntryDTO> entries) {
        this.walletId = walletId;
        this.title = title;
        this.fullName = fullName;
        this.email = email;
        this.entries = entries;
    }

    public WalletDTO(@NotBlank(message = "Title is required") String title, @NotBlank(message = "Full name is required") String fullName, @Email String email) {
        this.title = title;
        this.fullName = fullName;
        this.email = email;
    }
}
