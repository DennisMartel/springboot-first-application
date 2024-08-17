package com.martel.curso.service;

import com.martel.curso.exceptions.ProductException;
import com.martel.curso.model.Product;
import com.martel.curso.model.Category;
import com.martel.curso.repository.CategoryRepository;
import com.martel.curso.repository.ProductRepository;
import com.martel.curso.request.CreateProductRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImplementation implements ProductService {
    private final ProductRepository productRepository;
    private UserService userService;
    private final CategoryRepository categoryRepository;

    public ProductServiceImplementation(ProductRepository productRepository, UserService userService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public Product save(CreateProductRequest request) {
        Category topLevel = categoryRepository.findByName(request.getTopLevelCategory());
        
        if (topLevel == null) {
            Category topLavelCategory = new Category();
            topLavelCategory.setName(request.getTopLevelCategory());
            topLavelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLavelCategory);
        }
        
        Category secondLevel = categoryRepository.findByNameAndParent(request.getSecondLevelCategory(), topLevel.getName());
        
        if (secondLevel == null) {
            Category secondLavelCategory = new Category();
            secondLavelCategory.setName(request.getSecondLevelCategory());
            secondLavelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLavelCategory);
        }
        
        Category thirdLevel = categoryRepository.findByNameAndParent(request.getThirdLevelCategory(), secondLevel.getName());
        
        if (thirdLevel == null) {
            Category thirdLavelCategory = new Category();
            thirdLavelCategory.setName(request.getSecondLevelCategory());
            thirdLavelCategory.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLavelCategory);
        }
        
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setDescription(request.getDescription());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setDiscountPersent(request.getDiscountPersent());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setSizes(request.getSizes());
        product.setQuantity(request.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        
        return savedProduct;
    }

    @Override
    public String destroy(Long productId) throws ProductException {
        Product product = findById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product deleted successfully";
    }

    @Override
    public Product update(Long productId, Product request) throws ProductException {
        Product product = findById(productId);
        
        if (request.getQuantity() != 0) {
            product.setQuantity(request.getQuantity());
        }
        
        return productRepository.save(product);
    }

    @Override
    public Product findById(Long productId) throws ProductException {
        Optional<Product> opt = productRepository.findById(productId);
        
        if (opt.isPresent()) {
            return opt.get();
        }
        
        throw new ProductException("Product not found with id -" + productId);
    }

    @Override
    public List<Product> findByCategory(String category) {
       return null;
    }

    @Override
    public Page<Product> getAll(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maximunPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maximunPrice, minDiscount, sort);
        
        if (!colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }
        
        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }
        
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        
        List<Product> pageContent = products.subList(startIndex, endIndex);
        
        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
        
        return filteredProducts;
    }
    
}
