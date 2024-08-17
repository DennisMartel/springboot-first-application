package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Product;
import com.martel.curso.request.CreateProductRequest;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {
    public Product save(CreateProductRequest request);
    
    public String destroy(Long productId) throws ProductException;
    
    public Product update(Long productId, Product request) throws ProductException;
    
    public Product findById(Long productId) throws ProductException;
    
    public List<Product> findByCategory(String category);
    
    public Page<Product> getAll(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);
}
