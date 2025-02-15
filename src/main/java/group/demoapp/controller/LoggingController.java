package group.demoapp.controller;

import group.demoapp.security.User;
import group.demoapp.service.SecurityUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LoggingController {
    private SecurityUserService securityUserService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/for_logged_user")
    public ResponseEntity<String> for_logged_user() {
        return ResponseEntity.ok("User logged in and can see this");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody User user) {
        String s = securityUserService.verifyUserAndGetJWT(user);
        return ResponseEntity.ok(s);
    }
}
