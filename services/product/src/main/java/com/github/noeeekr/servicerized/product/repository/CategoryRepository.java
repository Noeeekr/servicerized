package com.github.noeeekr.servicerized.product.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.models.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.category_id = :categoryId AND c.deleted_at == NULL")
    List<CategoryEntity> findCategory(@Param("categoryId") UUID categoryId);

    @Query("SELECT c FROM CategoryEntity c WHERE c.category_name = :categoryName AND c.deleted_at == NULL")
    List<CategoryEntity> findCategory(@Param("categoryName") String categoryName);

    @Query("SELECT c FROM CategoryEntity c WHERE c.category_name = :categoryName AND c.category_id = :categoryId AND c.deleted_at == NULL")
    List<CategoryEntity> findCategory(@Param("categoryName") String categoryName,
            @Param("categoryId") UUID categoryId);
}
