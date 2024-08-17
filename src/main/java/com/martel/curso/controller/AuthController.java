package com.martel.curso.controller;

import com.martel.curso.response.AuthResponse;
import com.martel.curso.config.JwtProvider;
import com.martel.curso.exceptions.UserException;
import com.martel.curso.model.User;
import com.martel.curso.repository.UserRepository;
import com.martel.curso.request.LoginRequest;
import com.martel.curso.service.CustomeUserServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomeUserServiceImplementation customeUserService;
    
    public AuthController(
        UserRepository userRepository,
        CustomeUserServiceImplementation customeUserService, 
        PasswordEncoder passwordEncoder,
        JwtProvider jwtProvider
    ) {
        this.userRepository = userRepository;
        this.customeUserService = customeUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }
    
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        
        User isEmailExist = userRepository.findByEmail(email);
        
        if (isEmailExist != null) {
            throw new UserException("Email is already used with another account");
        }
        
        User us = new User();
        us.setEmail(email);
        us.setPassword(passwordEncoder.encode(password));
        us.setFirstName(firstName);
        us.setLastName(lastName);
        User savedUser = userRepository.save(us);
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token  = jwtProvider.generateToken(authentication);
        
        AuthResponse authResponse = new AuthResponse();
        
        authResponse.setToken(token);
        authResponse.setMessage("Signup success");
        
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        Authentication authentication = authenticate(username, password); 
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token  = jwtProvider.generateToken(authentication);
        
        AuthResponse authResponse = new AuthResponse();
        
        authResponse.setToken(token);
        authResponse.setMessage("Signin success");
        
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customeUserService.loadUserByUsername(username);
        
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
