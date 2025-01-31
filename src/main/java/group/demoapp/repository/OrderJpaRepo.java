package group.demoapp.repository;

import group.demoapp.repository.entity.Order;
import group.demoapp.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepo extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
