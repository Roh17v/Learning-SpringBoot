package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.User;
import com.rohitverma.journal.service.UserDetailsServiceImpl;
import com.rohitverma.journal.service.UserService;
import com.rohitverma.journal.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
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
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try{
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }

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

        // Optional: You can also send token in the body if needed
        return ResponseEntity.ok("Login successful. JWT set in cookie.");
    }

}
