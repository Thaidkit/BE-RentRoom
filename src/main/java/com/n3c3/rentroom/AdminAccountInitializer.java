package com.n3c3.rentroom;

import com.n3c3.rentroom.entity.Role;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.UserRepository;
import com.n3c3.rentroom.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class AdminAccountInitializer implements CommandLineRunner {

    private final Logger log = Logger.getLogger(AdminAccountInitializer.class.getName());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        final String adminEmail = "admin@gmail.com";
        final String adminPhone = "0999999999";

        if (userRepository.findByEmail(adminEmail).isEmpty() && userRepository.findByPhone(adminPhone).isEmpty()) {
            User admin = new User();
            admin.setId(1L);
            admin.setFullName("Admin");
            admin.setEmail(adminEmail);
            admin.setPhone(adminPhone);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setActived(Boolean.TRUE);
            admin.setRole(Role.ADMIN);
            admin.setTotalMoney(0L);
            userRepository.save(admin);

            log.info("Admin account created successfully!");
        } else {
            log.info("Admin account already exists!");
        }
    }
}
