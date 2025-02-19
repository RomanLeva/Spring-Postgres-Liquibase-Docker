package group.demoapp.controller;

import group.demoapp.security.User;
import group.demoapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LoggingController {
    private AuthService authService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/for_logged_user")
    public ResponseEntity<String> for_logged_user() {
        return ResponseEntity.ok("User logged in and can see this");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/for_logged_admin")
    public ResponseEntity<String> for_logged_admin() {
        return ResponseEntity.ok("User admin in and can see this");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String jwt = authService.verifyUserAndGetJWT(user);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        authService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/restore_account_by_admin")
    public ResponseEntity<String> restore_account_by_admin(@RequestBody User user) {
        User loaded = authService.getUserByUsername(user.getUsername(), user.getPassword());
        loaded.setAccountNonBlocked(true);
        return ResponseEntity.ok("Account restored by admin");
    }
}
