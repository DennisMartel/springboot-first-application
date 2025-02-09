package com.martel.curso.repository;

import com.martel.curso.model.Rating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long>{
    @Query("SELECT r FROM Rating r WHERE r.product.id = :productId")
    public List<Rating> getAllProductsRating(@Param("productId") Long productId);
}
