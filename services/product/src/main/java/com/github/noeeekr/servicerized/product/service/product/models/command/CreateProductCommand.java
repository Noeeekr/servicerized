package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProductCommand implements CreateProductCommandInterface {
    private UUID productId;
    private UUID productOwnerId;

    private String productName;
    private String productDescription;
    
    private Integer productPrice;
}
