package com.github.noeeekr.servicerized.product.controller.models.dto;

import com.github.noeeekr.servicerized.response.client.ClientResponseDto;

public class DeleteVirtualProductDto implements ClientResponseDto {
    public int deletedAmount;

    public DeleteVirtualProductDto(int deletedAmount) {
        this.deletedAmount = deletedAmount;
    }

    public Object prepareToClient() {
        return this;
    }
}
