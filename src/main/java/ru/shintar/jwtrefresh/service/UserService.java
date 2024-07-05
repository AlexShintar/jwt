package ru.shintar.jwtrefresh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shintar.jwtrefresh.model.entity.Role;
import ru.shintar.jwtrefresh.model.entity.User;
import ru.shintar.jwtrefresh.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(String username, String password) throws IllegalStateException {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists");
        }
        User user = User.builder()
                .username(username)
                .password(password)
                .roles(Set.of(Role.USER))
                .build();
        userRepository.save(user);
        return user;
    }

//    @Transactional
//    public void updateUser(User update) {
//        User user = loadUserByUsername(update.getUsername());
//        user.setUsername("bob");
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(user);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!");
//        userRepository.save(user);
//    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }
}
