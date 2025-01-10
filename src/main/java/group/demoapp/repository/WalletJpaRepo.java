package group.demoapp.repository;

import group.demoapp.repository.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface WalletJpaRepo extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {
    
    Wallet findByUuid(Long uuid);

    @Modifying
    @Transactional
    @Query("update #{#entityName} w SET w.amount = w.amount + :amount where w.uuid=:uuid")
    int depositWalletByUuid(@Param("uuid") Long uuid, @Param("amount") BigDecimal amount);

    @Modifying
    @Transactional
    @Query("update #{#entityName} w SET w.amount = w.amount - :amount where w.uuid=:uuid and w.amount - :amount > :amountMoreThanCondition")
    int withdrawWalletByUuid(@Param("uuid") Long uuid, @Param("amount") BigDecimal amount, @Param("amountMoreThanCondition") BigDecimal amountMoreThanCondition);
}
