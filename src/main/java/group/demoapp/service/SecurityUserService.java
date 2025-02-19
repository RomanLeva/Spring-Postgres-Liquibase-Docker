package group.demoapp.service;

import group.demoapp.security.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final Set<User> users = new HashSet<>();

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        if (optionalUser.isPresent()) return optionalUser.get();
        else throw new UsernameNotFoundException("User not found");
    }

    public void saveUser(User newUser) {
        users.add(newUser);
    }
}
