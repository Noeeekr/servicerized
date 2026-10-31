package com.github.noeeekr.servicerized.product.controller.models.request;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.query.update.UpdateProductFields;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ProductFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class UpdateProductRequest implements UpdateProductRequestInterface {

    private UpdateProductFields fields;
    private ProductFilter filter;

    public UpdateProductRequest(UpdateProductFields fields, ProductFilter filter) {
        this.fields = fields;
        this.filter = filter;
    }

    public UpdateProductRequest(UpdateProductFieldsInterface fields,
            ProductFilterableFieldsInterface filter) {
        this.fields = new UpdateProductFields(fields);
        this.filter = new ProductFilter(filter);
    }

    public ProductFilter getProductFilter() {
        return this.filter;
    }

    public UpdateProductFields getProduct() {
        return this.fields;
    }
}
