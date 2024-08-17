package com.martel.curso.service;

import com.martel.curso.config.JwtProvider;
import com.martel.curso.exceptions.UserException;
import com.martel.curso.model.User;
import com.martel.curso.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService{
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        
        if (user.isPresent()) {
            return user.get();
        }
        
        throw new UserException("user not found with id -" + id);
    }

    @Override
    public User findUserProfiByJwt(String tkn) throws UserException {
        String email = jwtProvider.getEmailFromToken(tkn);
        
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            throw new UserException("user not found with email -" + email);
        }
        
        return user;
    }
    
}
