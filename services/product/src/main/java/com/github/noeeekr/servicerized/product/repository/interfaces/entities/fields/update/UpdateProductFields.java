package com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.update;

import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductFieldsInterface;

public class UpdateProductFields implements UpdateProductFieldsInterface {
    public String productName;
    public String productDescription;

    public Integer productPrice;

    public UpdateProductFields(String productName, String productDescription,
            Integer productPrice) {
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productName = productName;
    }

    public UpdateProductFields(UpdateProductFieldsInterface product) {
        this.productDescription = product.getProductDescription();
        this.productPrice = product.getProductPrice();
        this.productName = product.getProductName();
    }

    public String getProductName() {
        return this.productName;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public Integer getProductPrice() {
        return this.productPrice;
    }
}
