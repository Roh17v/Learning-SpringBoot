package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.User;
import com.rohitverma.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users;
    }
}
