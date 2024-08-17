package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Cart;
import com.martel.curso.model.CartItem;
import com.martel.curso.model.Product;
import com.martel.curso.model.User;
import com.martel.curso.repository.CartRepository;
import com.martel.curso.request.AddCartRequest;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation implements CartService{
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;

    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }
    
    @Override
    public Cart save(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String add(Long userId, AddCartRequest request) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findById(request.getProductId());
        
        CartItem inCart = cartItemService.inCart(cart, product, request.getSize(), userId);
        
        if (inCart == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setUserId(userId);
            
            int price = request.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(request.getSize());
            
            CartItem createdItem = cartItemService.save(cartItem);
            cart.getCartItems().add(createdItem);
        }
        
        return "Item added to cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;
        
        for (CartItem cartItem :cart.getCartItems()) {
            totalPrice = totalPrice + cartItem.getPrice();
            totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice();
            totalItem = totalItem + cartItem.getQuantity();
        }
        
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscounted(totalPrice -  totalDiscountedPrice);
        
        return cartRepository.save(cart);
    }
    
}
