package com.github.noeeekr.servicerized.product.controller.models.dto;

import com.github.noeeekr.servicerized.product.repository.models.dto.KindDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.ProductDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductInformationDto;

/**
 * VirtualProductClientDto extends VirtualProductDto making it a client safe class.
 */
public class VirtualProductInformationClientDto extends VirtualProductInformationDto {
    public VirtualProductInformationClientDto(VirtualProductDto productKindDto,
            ProductDto productDto, KindDto kindDto) {
        super(productKindDto, productDto, kindDto);
    }
}
