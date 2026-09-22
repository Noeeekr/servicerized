package com.github.noeeekr.servicerized.product.controller.request;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.CreateProductInterface;
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
