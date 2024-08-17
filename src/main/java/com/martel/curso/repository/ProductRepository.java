package com.martel.curso.repository;

import com.martel.curso.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
    @Query("SELECT p FROM Product p " +
        "WHERE (p.category.name = :category OR :category=' ') " +
        "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
        "AND (:minDiscount IS NULL OR p.discountPersent >= :minDiscount) " +
        "ORDER BY " +
        "CASE WHEN :sort = 'low_price' THEN p.discountedPrice END ASC, " +
        "CASE WHEN :sort = 'high_price' THEN p.discountedPrice END DESC")
    public List<Product> filterProducts(
        @Param("category") String category,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice,
        @Param("minDiscount") Integer minDiscount,
        @Param("sort") String sort
    );
}
