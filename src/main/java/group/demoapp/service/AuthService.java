package group.demoapp.service;

import group.demoapp.security.JwtUtils;
import group.demoapp.security.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private SecurityUserService securityUserService;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;

    public String verifyUserAndGetJWT(User userToVerify) {
        User user = securityUserService.loadUserByUsername(userToVerify.getUsername());
        boolean matches = passwordEncoder.matches(userToVerify.getPassword(), user.getPassword());

        if (matches) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(userToVerify.getUsername())
                    .authorities(userToVerify.getAuthorities().toString())
                    .password(userToVerify.getPassword()).build();
            return jwtUtils.generateToken(userDetails);
        }
        else {
            return "Wrong credentials";
        }
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        securityUserService.saveUser(user);
    }

    public User getUserByUsername(String username, String password) {
        User loadedUser = securityUserService.loadUserByUsername(username);
        boolean matches = passwordEncoder.matches(password, loadedUser.getPassword());
        if (matches) {
            return loadedUser;
        } else {
            throw new UsernameNotFoundException(username);
        }

    }
}
