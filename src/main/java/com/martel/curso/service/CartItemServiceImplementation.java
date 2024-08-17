package com.martel.curso.service;

import com.martel.curso.exceptions.CartItemException;
import com.martel.curso.exceptions.UserException;
import com.martel.curso.model.Cart;
import com.martel.curso.model.CartItem;
import com.martel.curso.model.Product;
import com.martel.curso.model.User;
import com.martel.curso.repository.CartItemRepository;
import com.martel.curso.repository.CartRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImplementation implements CartItemService{
    private CartItemRepository cartItemRepository;
    private UserService userService;
    private CartRepository cartRepository;
    
    public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }
 
    @Override
    public CartItem save(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
        
        CartItem createdItem = cartItemRepository.save(cartItem);
        return createdItem;
    }

    @Override
    public CartItem update(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findById(id);
        User user = userService.findUserById(userId);
        
        if (user.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
        }
        
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem inCart(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem = cartItemRepository.inCart(cart, product, size, userId);
        return cartItem;
    }

    @Override
    public void destroy(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = findById(cartItemId);
        User user = userService.findUserById(cartItem.getUserId());
        
        User reqUser = userService.findUserById(user.getId());
        
        if (user.getId().equals(reqUser.getId())) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new UserException("you can't remove another users item");
        }
    }

    @Override
    public CartItem findById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);
        
        if (opt.isPresent()) {
            return opt.get();
        }
        
        throw new CartItemException("cart item not found with id -" + cartItemId);
    }
    
}
