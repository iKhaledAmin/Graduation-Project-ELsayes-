package com.GP.ELsayes.security;

import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUserName(username);
        user.orElseThrow((() -> new UsernameNotFoundException("User not found.")));

        if (user.isPresent()) {
            User existingUser = user.get();
            return CustomUserDetails.builder()
                    .id(existingUser.getId())
                    .userName(existingUser.getUserName())
                    .userRole(existingUser.getUserRole())
                    .password(existingUser.getPassword())
                    .build();
        }
        return null;
    }
}
