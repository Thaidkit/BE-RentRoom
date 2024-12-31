package com.n3c3.rentroom.service;

import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);

    }
}
