package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Cart;
import com.martel.curso.model.User;
import com.martel.curso.request.AddCartRequest;

public interface CartService {
    public Cart save(User user);
    
    public String add(Long userId, AddCartRequest request) throws ProductException;
    
    public Cart findUserCart(Long userId);
}
