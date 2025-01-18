package group.demoapp.controller;

import group.demoapp.controller.mapper.MapperDtoToView;
import group.demoapp.controller.view.WalletViewModel;
import group.demoapp.service.WalletService;
import group.demoapp.service.dto.WalletChangeDto;
import group.demoapp.service.dto.WalletDto;
import group.demoapp.service.dto.WalletRegisterDto;
import group.demoapp.util.DateTimeEvent;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final MapperDtoToView viewMapper;

    @AllArgsConstructor
    @Getter
    public static class ControllerResponse {
        private String responseMessage;
    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<WalletViewModel> getWallet(@PathVariable Long WALLET_UUID) {
        WalletDto walletDto = walletService.getWalletById(WALLET_UUID);

        return ResponseEntity.ok(viewMapper.mapDtoToView(WalletViewModel.class, walletDto));
    }

    @GetMapping("/datetime")
    public ResponseEntity<DateTimeEvent> getDateTime() {
        return ResponseEntity.ok(new DateTimeEvent(LocalDateTime.now()));
    }

    @PostMapping("/register")
    public ResponseEntity<ControllerResponse> registerWallet(@Valid @RequestBody WalletRegisterDto walletRegisterDto) {
        WalletDto walletDto = walletService.registerWallet(walletRegisterDto);

        return ResponseEntity.ok(new ControllerResponse("New wallet saved with UUID " + walletDto.getUuid()));
    }

    @PostMapping("/wallet")
    public ResponseEntity<ControllerResponse> changeWallet(@Valid @RequestBody WalletChangeDto walletChangeDto) {
        walletService.updateWallet(walletChangeDto);

        return ResponseEntity.ok(new ControllerResponse("Wallet changed."));
    }
}
