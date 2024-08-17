package com.martel.curso.service;

import com.martel.curso.exceptions.UserException;
import com.martel.curso.model.User;

public interface UserService {
    public User findUserById(Long id) throws UserException;
    
    public User findUserProfiByJwt(String tkn) throws UserException;
}
