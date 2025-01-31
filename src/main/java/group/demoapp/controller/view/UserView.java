package group.demoapp.controller.view;

import group.demoapp.repository.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserView {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserSummary {
        private String name;
        private String email;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserDetails {
        private String name;
        private List<Order> orders;
    }
}
