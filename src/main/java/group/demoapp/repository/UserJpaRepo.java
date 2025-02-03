package group.demoapp.repository;

import group.demoapp.repository.entity.User;
import group.demoapp.repository.entity.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepo extends JpaRepository<User, Long> {

    UserProjection findByName(String name);
}
