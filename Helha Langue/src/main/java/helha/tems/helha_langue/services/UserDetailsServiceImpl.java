package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.User;

import helha.tems.helha_langue.repositories.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepository;


    public UserDetailsServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Create a UserDetails object with user details
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "", // No password required as per your requirement
                getAuthorities(user)); // Get user roles/authorities
    }

    // Get user roles/authorities, you can customize this based on your application
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // Return the roles of the user, for example:
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}


