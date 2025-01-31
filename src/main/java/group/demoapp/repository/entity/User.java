package group.demoapp.repository.entity;

import com.fasterxml.jackson.annotation.JsonView;
import group.demoapp.controller.view.UserView;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @Setter(AccessLevel.NONE)
    private Long id;

    @JsonView({UserView.UserSummary.class, UserView.UserDetails.class})
    private String name;

    @JsonView(UserView.UserSummary.class)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @Setter(AccessLevel.NONE)
    @JsonView(UserView.UserDetails.class)
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

}
