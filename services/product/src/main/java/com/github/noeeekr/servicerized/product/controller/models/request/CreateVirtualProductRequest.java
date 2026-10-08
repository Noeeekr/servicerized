package com.github.noeeekr.servicerized.product.controller.models.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateVirtualProductRequest implements CreateVirtualProductRequestInterface {
    private String productName;
    private Integer productPrice;
    private String productDescription;
    private int provisionHours;
}
