package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Rating;
import com.martel.curso.model.Product;
import com.martel.curso.model.User;
import com.martel.curso.repository.RatingRepository;
import com.martel.curso.request.RatingRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImplementation implements RatingService {
    private RatingRepository ratingRepository;
    private ProductService productService;
    
    public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating save(RatingRequest request, User user) throws ProductException {
        Product product = productService.findById(request.getProductId());
        
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(request.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        return ratingRepository.getAllProductsRating(productId);
    }
    
}
