package group.demoapp.service;

import group.demoapp.security.JwtUtils;
import group.demoapp.security.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private JwtUtils jwtUtils;

    // Word "password" was encrypted by BCrypt with strength 4
//    $2y$04$ca64ez6L7rJFqv6DODFl3OtjmjlsE321xp6JmObaUmjzth3V5D7Oy
    private final User user = new User("username", "password", User.Roles.ROLE_USER, true);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return user;
    }

    public String verifyUserAndGetJWT(User userToVerify) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4 );
//        userToVerify.setPassword(encoder.encode(userToVerify.getPassword()));
//
//        System.out.println("PASS AFTER BCRYPT " + userToVerify.getPassword());

        if (userToVerify.getPassword().equals(user.getPassword())) {
            return jwtUtils.generateToken(userToVerify);
        }
        else {
            return "Wrong credentials";
        }
    }
}
