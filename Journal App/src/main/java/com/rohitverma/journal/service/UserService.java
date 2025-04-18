package com.rohitverma.journal.service;

import ch.qos.logback.classic.Logger;
import com.rohitverma.journal.model.User;
import com.rohitverma.journal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveNewUser(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        }
        catch(Exception e){
            log.error("hahaha");
            log.info("hahaha");
            log.trace("hahaha");
            log.debug("hahaha");
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }


    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
