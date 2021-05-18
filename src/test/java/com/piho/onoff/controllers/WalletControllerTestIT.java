package com.piho.onoff.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piho.onoff.domain.DTO.EntryDTO;
import com.piho.onoff.domain.DTO.WalletDTO;
import com.piho.onoff.services.WalletService;
import net.minidev.json.JSONArray;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTestIT {

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;
    private final Object JSONArray = new JSONArray();

    @Test
    void createWallet() throws Exception {
        WalletDTO walletToPost = new WalletDTO("MyWallet", "Valentin Piho", "val.piho@gmail.com");
        WalletDTO walletToReturn = new WalletDTO("3231231231", "MyWallet", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());
        doReturn(walletToReturn).when(walletService).createWallet(any());

        mockMvc.perform(post("/api/wallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(walletToPost)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.walletId", matchesPattern("^[0-9]{10}$")))
                .andExpect(jsonPath("$.title", is("MyWallet")))
                .andExpect(jsonPath("$.fullName", is("Valentin Piho")))
                .andExpect(jsonPath("$.email", is("val.piho@gmail.com")))
                .andExpect(jsonPath("$.entries", is(JSONArray)));

    }

    @Test
    void getWalletById() throws Exception {
        WalletDTO wallet = new WalletDTO("3231231231", "MyWallet", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());
        doReturn(wallet).when(walletService).getWalletByWalletId("3231231231");

        mockMvc.perform(get("/api/wallet/{walletId}", "3231231231"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.walletId", matchesPattern("^[0-9]{10}$")))
                .andExpect(jsonPath("$.title", is("MyWallet")))
                .andExpect(jsonPath("$.fullName", is("Valentin Piho")))
                .andExpect(jsonPath("$.email", is("val.piho@gmail.com")))
                .andExpect(jsonPath("$.entries", is(JSONArray)));
    }

    @Test
    void getWalletEntriesList() {
        WalletDTO wallet = new WalletDTO("3231231231", "MyWallet", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());
        EntryDTO entry1 = new EntryDTO();
    }

    @Test
    void getWalletsList() throws Exception {
        WalletDTO wallet = new WalletDTO("3231231231", "MyWallet", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());
        WalletDTO wallet1 = new WalletDTO("3231231230", "MyWallet1", "Valentin Piho", "val.piho@gmail.com", new ArrayList<>());
        doReturn(Lists.newArrayList(wallet, wallet1)).when(walletService).getWalletsList();

        mockMvc.perform(get("/api/wallet/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].walletId", matchesPattern("^[0-9]{10}$")))
                .andExpect(jsonPath("$[0].title", is("MyWallet")))
                .andExpect(jsonPath("$[0].fullName", is("Valentin Piho")))
                .andExpect(jsonPath("$[0].email", is("val.piho@gmail.com")))
                .andExpect(jsonPath("$[0].entries", is(JSONArray)))
                .andExpect(jsonPath("$[1].walletId", matchesPattern("^[0-9]{10}$")))
                .andExpect(jsonPath("$[1].title", is("MyWallet1")))
                .andExpect(jsonPath("$[1].fullName", is("Valentin Piho")))
                .andExpect(jsonPath("$[1].email", is("val.piho@gmail.com")))
                .andExpect(jsonPath("$[1].entries", is(JSONArray)));
    }

    @Test
    void updateWallet() throws Exception {
        WalletDTO walletToPost = new WalletDTO("MyWallet", "Valentin Piho", "val.piho@gmail.com");
        WalletDTO walletToUpdate = new WalletDTO("3231231231", "YourWallet", "Aleksander Shown", "alex.shown@gmail.com", new ArrayList<>());


        doReturn(walletToUpdate).when(walletService).updateWallet("3231231231", walletToPost);
        mockMvc.perform(patch("/api/wallet/{walletId}", "3231231231")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(walletToPost)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.walletId", matchesPattern("^[0-9]{10}$")))
                .andExpect(jsonPath("$.title", is("YourWallet")))
                .andExpect(jsonPath("$.fullName", is("Aleksander Shown")))
                .andExpect(jsonPath("$.email", is("alex.shown@gmail.com")))
                .andExpect(jsonPath("$.entries", is(JSONArray)));;
    }

    @Test
    void deleteWallet() throws Exception {
        mockMvc.perform(delete("/api/wallet/{walletId}", "3231231231"))
                .andExpect(status().isOk());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
