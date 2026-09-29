package com.github.noeeekr.servicerized.product.repository.models.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.relations.VirtualProductInformationInterface;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class VirtualProductInformationDto implements VirtualProductInformationInterface {
    @Delegate(excludes = {VirtualProductFieldsInterface.Id.class})
    @JsonUnwrapped
    private VirtualProductDto productKindDto;

    @Delegate
    @JsonUnwrapped
    private ProductDto productDto;

    @Delegate
    @JsonUnwrapped
    private KindDto kindDto;

    public VirtualProductInformationDto(VirtualProductDto productKindDto, ProductDto productDto,
            KindDto kindDto) {
        this.productKindDto = productKindDto;
        this.productDto = productDto;
        this.kindDto = kindDto;
    }

    public VirtualProductInformationDto(VirtualProductInformationDto dto) {
        this.productKindDto = dto.productKindDto;
        this.productDto = dto.productDto;
        this.kindDto = dto.kindDto;
    }
}
