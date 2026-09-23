package com.github.noeeekr.servicerized.product.repository;

import java.util.List;
import java.util.UUID;
import org.hibernate.query.spi.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("Select p from Products p where p.product_id = :productId and p.deleted_at = null")
    List<ProductEntity> findProduct(@Param("productId") UUID productId, Limit limit);

    @Query("Select p from Products p where p.product_owner_id = :ownerId and p.product_id = :productId and p.deleted_at = null")
    List<ProductEntity> findProduct(@Param("productId") UUID productId,
            @Param("ownerId") UUID ownerId, Limit limit);
}
