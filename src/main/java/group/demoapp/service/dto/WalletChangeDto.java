package group.demoapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WalletChangeDto {
    public enum OPERATION_TYPE {DEPOSIT, WITHDRAW}

    private Long uuid;
    private BigDecimal amount;
    private OPERATION_TYPE operationType;
}
