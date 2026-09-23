package com.github.noeeekr.servicerized.product.repository.interfaces;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.controller.request.CreateProductInterface;

public interface ProductInterface extends CreateProductInterface {
    public UUID getId();
}
