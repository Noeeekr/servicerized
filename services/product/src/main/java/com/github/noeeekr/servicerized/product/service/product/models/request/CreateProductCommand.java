package com.github.noeeekr.servicerized.product.service.product.models.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter 
@AllArgsConstructor
public class CreateProductCommand implements CreateProductCommandInterface {
    private UUID id;
    private UUID ownerId;
    private String name;
    private Integer price;
    private String description;
}

