package group.demoapp.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import group.demoapp.controller.view.UserView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    public enum Authorities {USER, ADMIN, MODERATOR}

    private String username;
    private String password;
    private Authorities authorities;
    private boolean isAccountNonBlocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User user)) return false;
        if (Objects.equals(username, user.username)) return false;
        if (Objects.equals(password, user.password)) return false;
        if (Objects.equals(authorities, user.authorities)) return false;
        return false;
    }
}
