package group.demoapp.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wallet {

    @Id
    @SequenceGenerator(name = "wallets_seq", sequenceName = "wallets_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallets_seq")
    @Setter(AccessLevel.NONE)
    private Long uuid;

    private BigDecimal amount;

}
