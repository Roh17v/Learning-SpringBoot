package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.User;

import com.rohitverma.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User userFound = userService.findUserByUsername(username);

            userFound.setUsername(user.getUsername());
            userFound.setPassword(user.getPassword());
            userService.saveUser(userFound);
            return new ResponseEntity<>(userFound, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            userService.deleteUserByUsername(auth.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
