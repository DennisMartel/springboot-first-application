package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Product;
import com.martel.curso.model.Review;
import com.martel.curso.model.User;
import com.martel.curso.repository.ProductRepository;
import com.martel.curso.repository.ReviewRepository;
import com.martel.curso.request.ReviewRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImplementation implements ReviewService{
    private ReviewRepository reviewRepository;
    private ProductService productService;
    private ProductRepository productRepository;
    
    public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }
    
    @Override
    public Review save(ReviewRequest request, User user) throws ProductException {
         Product product = productService.findById(request.getProductId());
         
         Review review = new Review();
         review.setUser(user);
         review.setProduct(product);
         review.setReview(request.getReview());
         review.setCreatedAt(LocalDateTime.now());
         
         return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews(Long productId) {
        return reviewRepository.getAllProductsReview(productId);
    }
}
