package com.github.noeeekr.servicerized.product.repository.interfaces;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.service.product.models.request.CreateProductCommandInterface;

public interface ProductInterface extends CreateProductCommandInterface {
    public UUID getId();
}
