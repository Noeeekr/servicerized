package com.github.noeeekr.servicerized.product.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
