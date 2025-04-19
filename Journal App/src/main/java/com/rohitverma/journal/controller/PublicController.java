package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.User;
import com.rohitverma.journal.service.UserDetailsServiceImpl;
import com.rohitverma.journal.service.UserService;
import com.rohitverma.journal.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("health-check")
    public String getHealth()
    {
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletResponse response) {
        try {
            User userToSave = new User();
            userToSave.setUsername(user.getUsername());
            userToSave.setPassword(user.getPassword());

            userService.saveNewUser(userToSave);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            final String jwt = jwtUtils.generateToken(userDetails);

            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);

            return new ResponseEntity<>(userToSave, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username or email already exists");
        } catch (Exception e) {
            log.error("Error: ",e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            final String jwt = jwtUtils.generateToken(userDetails);

            // Create a cookie with JWT
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true); // Prevent JavaScript access (security)
            cookie.setSecure(false); // Set to true if using HTTPS
            cookie.setPath("/"); // Make cookie accessible to the entire application
            cookie.setMaxAge(60 * 60); // 60 minutes (1 hour)

            // Add cookie to response
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful. JWT set in cookie.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

}
