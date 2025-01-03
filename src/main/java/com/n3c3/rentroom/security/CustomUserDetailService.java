package com.n3c3.rentroom.security;

import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String value) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneOrEmail(value);
        if (user == null) {
            throw new UsernameNotFoundException(value);
        }
        return new CustomUserDetails(user);
    }

    public CustomUserDetails loadUserByPhoneOrEmail(String value) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneOrEmail(value);
        if (user == null) {
            throw new UsernameNotFoundException(value);
        }
        return new CustomUserDetails(user);
    }
}
