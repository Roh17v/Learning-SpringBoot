package com.rohitverma.journal.controller;

import com.rohitverma.journal.api.response.WeatherResponse;
import com.rohitverma.journal.model.User;

import com.rohitverma.journal.service.UserService;
import com.rohitverma.journal.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Autowired
    WeatherService weatherService;

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User userFound = userService.findUserByUsername(username);

            userFound.setUsername(user.getUsername());
            userFound.setPassword(user.getPassword());
            userService.saveNewUser(userFound);
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

    @GetMapping
    public ResponseEntity<?> greetUser(@RequestParam String city) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            WeatherResponse weather = weatherService.getWeather(city);
            String greeting = "Hi " + auth.getName() + ". Today the  weather of "+ weather.getLocation().getName() +" is : "+ weather.getCurrent().getTemperature();
            return new ResponseEntity<>(greeting,HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response)
    {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);

        response.addCookie(cookie);

        SecurityContextHolder.getContext().setAuthentication(null);

        return new ResponseEntity<>("Logged out Successfully!",HttpStatus.OK);
    }
}
