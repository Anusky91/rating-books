package es.anusky.rating_books.users.infrastructure.security;

import es.anusky.rating_books.users.infrastructure.persistence.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SpringDataUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByAlias(username)
                .map(BookStarUserDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with email " + username + " not found"));
    }

}
