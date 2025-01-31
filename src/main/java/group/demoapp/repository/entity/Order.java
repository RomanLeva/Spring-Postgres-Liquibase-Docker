package group.demoapp.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import group.demoapp.controller.view.UserView;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @SequenceGenerator(name = "orders_seq", sequenceName = "orders_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    @Setter(AccessLevel.NONE)
    private Long id;

    @JsonView(UserView.UserDetails.class)
    private String info;

    @JsonView(UserView.UserDetails.class)
    private String status;

    @JsonView(UserView.UserDetails.class)
    private Integer sum;

    @ManyToOne(fetch = FetchType.LAZY ,optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
