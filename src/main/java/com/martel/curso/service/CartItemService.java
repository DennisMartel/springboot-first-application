package com.martel.curso.service;

import com.martel.curso.exceptions.CartItemException;
import com.martel.curso.exceptions.UserException;
import com.martel.curso.model.Cart;
import com.martel.curso.model.CartItem;
import com.martel.curso.model.Product;

public interface CartItemService {
    public CartItem save(CartItem cartItem);
    
    public CartItem update(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;
    
    public CartItem inCart(Cart cart, Product product, String size, Long userId);
    
    public void destroy(Long userId, Long cartItemId) throws CartItemException, UserException;
    
    public CartItem findById(Long cartItemId) throws CartItemException;
}
