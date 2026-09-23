package com.github.noeeekr.servicerized.product.repository;

import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.models.ProductCategoryEntity;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, UUID> {
        @Query("SELECT pc from ProductCategoryEntity pc WHERE pc.product_id = :productId AND pc.category_id = :categoryId AND pc.deleted_at = null")
        public ProductCategoryEntity findRelation(@Param("productId") UUID productId,
                        @Param("categoryId") UUID categoryId, Limit limit);

        @Query("SELECT pc from ProductCategoryEntity pc JOIN pc.category c WHERE pc.product_id = :productId AND c.category_name = :categoryName AND pc.deleted_at = null")
        public ProductCategoryEntity findRelation(@Param("productId") UUID productId,
                        @Param("categoryName") String categoryName, Limit limit);
}
