package com.github.noeeekr.servicerized.product.controller.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter 
@AllArgsConstructor
public class CreateProductRequest implements CreateProductInterface {
    private UUID id;
    private UUID ownerId;
    private String name;
    private Integer price;
    private String description;
}

