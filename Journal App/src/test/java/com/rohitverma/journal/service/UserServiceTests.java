package com.rohitverma.journal.service;

import com.rohitverma.journal.model.User;
import com.rohitverma.journal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

   @ParameterizedTest
   @ValueSource(strings = {
           "rohitverma",
           "ayushverma",
           "premverma"
   })
    public void testGetbyUsername(String username) {
       User user = userRepository.findByUsername(username);
       assertNotNull(user);
   }
}
