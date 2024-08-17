package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Review;
import com.martel.curso.model.User;
import com.martel.curso.request.ReviewRequest;
import java.util.List;

public interface ReviewService {
    public Review save(ReviewRequest request, User user) throws ProductException;
    
    public List<Review> getAllReviews(Long productId);
}
