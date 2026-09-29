package com.github.noeeekr.servicerized.product.repository.models.relations;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.relations.VirtualProductInformationInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.KindEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import lombok.experimental.Delegate;

public class VirtualProductInformation implements VirtualProductInformationInterface {
    @Delegate(excludes = {VirtualProductFieldsInterface.Id.class})
    private VirtualProductEntity productKind;

    @Delegate
    private ProductEntity product;

    @Delegate
    private KindEntity kind;

    public VirtualProductInformation(VirtualProductEntity productKind, ProductEntity product,
            KindEntity kind) {
        this.productKind = productKind;
        this.product = product;
        this.kind = kind;
    }
}
