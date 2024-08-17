package com.martel.curso.service;

import com.martel.curso.exceptions.OrderException;
import com.martel.curso.model.Address;
import com.martel.curso.model.Order;
import com.martel.curso.model.User;
import java.util.List;

public interface OrderService {
    public Order save(User user, Address address);
    
    public Order findById(Long id) throws OrderException;
    
    public List<Order> userOrderHistory(Long userId);
    
    public Order placed(Long id) throws OrderException;
    
    public Order confirmed(Long id) throws OrderException;
    
    public Order shipped(Long id) throws OrderException;
    
    public Order delivered(Long id) throws OrderException;
    
    public Order cancelled(Long id) throws OrderException;
    
    public List<Order> getAll();
    
    public void destroy(Long orderId) throws OrderException;
}
