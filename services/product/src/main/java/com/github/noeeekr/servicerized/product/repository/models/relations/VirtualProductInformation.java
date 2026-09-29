package com.github.noeeekr.servicerized.product.repository.models.relations;

import com.github.noeeekr.servicerized.product.controller.models.dto.VirtualProductInformationClientDto;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.relations.VirtualProductInformationInterface;
import com.github.noeeekr.servicerized.product.repository.models.dto.KindDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.ProductDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductDto;
import com.github.noeeekr.servicerized.product.repository.models.entities.KindEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.response.client.ClientResponseDto;
import lombok.experimental.Delegate;

public class VirtualProductInformation
        implements VirtualProductInformationInterface, ClientResponseDto {
    @Delegate(excludes = {VirtualProductFieldsInterface.Id.class, ClientResponseDto.class})
    private VirtualProductEntity productKind;

    @Delegate(excludes = {ClientResponseDto.class})
    private ProductEntity product;

    @Delegate(excludes = {ClientResponseDto.class})
    private KindEntity kind;

    public VirtualProductInformation(VirtualProductEntity productKind, ProductEntity product,
            KindEntity kind) {
        this.productKind = productKind;
        this.product = product;
        this.kind = kind;
    }

    @Override
    public Object prepareToClient() {
        return new VirtualProductInformationClientDto(new VirtualProductDto(this.productKind),
                new ProductDto(this.product), new KindDto(this.kind));
    }
}
