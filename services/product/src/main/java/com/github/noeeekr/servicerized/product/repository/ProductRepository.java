package com.github.noeeekr.servicerized.product.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("SELECT p FROM ProductEntity p WHERE p.id = :productId AND p.deletedAt = null")
    List<ProductEntity> findProduct(@Param("productId") UUID productId, Limit limit);

    @Query("SELECT p FROM ProductEntity p WHERE p.ownerId = :ownerId AND p.id = :productId AND p.deletedAt = null")
    List<ProductEntity> findProduct(@Param("productId") UUID productId,
            @Param("ownerId") UUID ownerId, Limit limit);
}
