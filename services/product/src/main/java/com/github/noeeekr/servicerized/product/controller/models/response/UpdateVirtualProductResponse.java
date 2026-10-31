package com.github.noeeekr.servicerized.product.controller.models.response;

import com.github.noeeekr.servicerized.response.client.ClientResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateVirtualProductResponse implements ClientResponseDto {
    private Integer updatedAmount;

    public Object prepareToClient() {
        return this;
    }
}
