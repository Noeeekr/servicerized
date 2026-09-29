package com.github.noeeekr.servicerized.product.controller.models.dto;

import com.github.noeeekr.servicerized.product.repository.models.dto.KindDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.ProductDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductInformationDto;
import com.github.noeeekr.servicerized.response.client.ClientResponseDto;

/**
 * VirtualProductClientDto extends VirtualProductDto making it a client safe class.
 */
public class VirtualProductInformationClientDto extends VirtualProductInformationDto
        implements ClientResponseDto {

    public VirtualProductInformationClientDto(VirtualProductDto productKindDto,
            ProductDto productDto, KindDto kindDto) {
        super(productKindDto, productDto, kindDto);
    }

    @Override
    public Object prepareToClient() {
        return this;
    }
}
