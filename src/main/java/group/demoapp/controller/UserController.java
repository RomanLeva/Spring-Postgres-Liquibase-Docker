package group.demoapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import group.demoapp.controller.view.UserView;
import group.demoapp.repository.entity.User;
import group.demoapp.service.UserService;
import group.demoapp.service.dto.UserSummaryDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @AllArgsConstructor
    @Getter
    public static class ControllerResponse {
        private String responseMessage;
    }

    @JsonView(UserView.UserSummary.class)
    @GetMapping("/users_without_orders")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @JsonView(UserView.UserSummary.class)
    @GetMapping("/users_without_orders_pageable")
    public ResponseEntity<List<User>> getAllUsersPageable(@RequestParam int page, @RequestParam int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);

        List<User> allUsers = userService.getAllUsers(pageable).getContent();
        return ResponseEntity.ok(allUsers);
    }

    @JsonView(UserView.UserDetails.class)
    @GetMapping("/get_user_with_orders/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @JsonView(UserView.UserSummary.class)
    @PostMapping("/create_user")
    public ResponseEntity<User> createUser (@RequestBody @Valid UserSummaryDto user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/update_user/{id}")
    public ResponseEntity<ControllerResponse> updateUser (@PathVariable Long id, @RequestBody @Valid UserSummaryDto userDetails) {
        userService.updateUser(id, userDetails);
        return ResponseEntity.ok(new ControllerResponse("User updated") );
    }

    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<ControllerResponse> deleteUser (@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ControllerResponse("User deleted"));
    }
}
