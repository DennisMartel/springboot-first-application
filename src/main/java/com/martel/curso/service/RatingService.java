package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Rating;
import com.martel.curso.model.User;
import com.martel.curso.request.RatingRequest;
import java.util.List;

public interface RatingService {
    public Rating save(RatingRequest request, User user) throws ProductException;
    
    public List<Rating> getProductsRating(Long productId);
}
