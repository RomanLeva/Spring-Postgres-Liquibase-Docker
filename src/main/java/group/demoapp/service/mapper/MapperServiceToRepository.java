package group.demoapp.service.mapper;

import group.demoapp.repository.entity.User;
import group.demoapp.repository.entity.Wallet;
import group.demoapp.service.dto.UserSummaryDto;
import group.demoapp.service.dto.WalletDto;
import group.demoapp.service.dto.WalletRegisterDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MapperServiceToRepository {
    private final ModelMapper modelMapper;

    public List<WalletDto> mapWalletToDtoList(List<Wallet> wallets){
        return wallets.stream().map(wallet ->
                modelMapper.map(wallet, WalletDto.class)).collect(Collectors.toList());
    }

    public WalletDto mapWalletToDto(Wallet wallet){
        WalletDto walletDto = new WalletDto();
        modelMapper.map(wallet, walletDto);
        return walletDto;
    }
    public Wallet mapWalletRegisterDtoToWallet(WalletRegisterDto walletRegisterDto) {
        Wallet wallet = new Wallet();
        modelMapper.map(walletRegisterDto, wallet);
        return wallet;
    }

    public User mapUserDtoToUser(UserSummaryDto userDto) {
        User user = new User();
        modelMapper.map(userDto, user);
        return user;
    }
}
