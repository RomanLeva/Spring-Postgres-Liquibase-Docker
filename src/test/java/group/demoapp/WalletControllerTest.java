package group.demoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.demoapp.service.dto.WalletChangeDto;
import group.demoapp.service.dto.WalletRegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void testGetWalletIsSuccessful() throws Exception {
        WalletRegisterDto walletRegisterDto = new WalletRegisterDto(BigDecimal.valueOf(100));
        mockMvc.perform(post("/api/v1/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletRegisterDto)))
                        .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallets/1")).andExpect(status().isOk());
    }

    @Test
    void testChangeWalletIsSuccessful() throws Exception {
        WalletChangeDto walletChangeDto = new WalletChangeDto(1L, BigDecimal.valueOf(100), WalletChangeDto.OPERATION_TYPE.DEPOSIT);
        WalletRegisterDto walletRegisterDto = new WalletRegisterDto(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletRegisterDto)))
                        .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletChangeDto)))
                        .andExpect(status().isOk());
    }

    @Test
    void testRequestJsonError() throws Exception {
        mockMvc.perform(post("/api/v1/register")
                        .contentType("application/json")
                        .content("""
                                {
                                    "invalidParamName":"100"
                                }"""))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType("application/json")
                        .content("""
                                {
                                    "invalidParamName":"100"
                                }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNonExistentWallet() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/0")).andExpect(status().isConflict());
    }

    @Test
    void testInsufficientFunds() throws Exception {
        WalletChangeDto walletChangeDto = new WalletChangeDto(1L, BigDecimal.valueOf(101), WalletChangeDto.OPERATION_TYPE.WITHDRAW);
        WalletRegisterDto walletRegisterDto = new WalletRegisterDto(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletRegisterDto)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletChangeDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testRegisterWalletIsSuccessful() throws Exception {
        WalletRegisterDto walletRegisterDto = new WalletRegisterDto(BigDecimal.valueOf(100));
        mockMvc.perform(post("/api/v1/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(walletRegisterDto)))
                .andExpect(status().isOk());
    }

}

