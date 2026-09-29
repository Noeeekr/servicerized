package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateVirtualProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.CreateVirtualProductInformationInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.relations.VirtualProductInformationInterface;
import com.github.noeeekr.servicerized.product.repository.models.dto.KindDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.ProductDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductDto;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductInformationDto;
import com.github.noeeekr.servicerized.product.repository.models.entities.KindEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

public interface CreateVirtualProductCommandInterface
                extends CreateVirtualProductInformationInterface {

        /**
         * CreateVirtualProductCommandAdapter satisfies java interface constraints.
         */
        @AllArgsConstructor 
        public final class VirtualProductInformationAdapter implements CreateVirtualProductCommandInterface {
                @Delegate 
                private VirtualProductInformationInterface virtualProductInformation;
        }

        //
        // Transformators
        //
        public static CreateVirtualProductCommandInterface upgrade(
                        CreateVirtualProductRequestInterface request, UUID productOwnerId) {
                VirtualProductDto virtualProductDto =
                                new VirtualProductDto(null, request.getProvisionHours());

                ProductDto productDto = new ProductDto(null, productOwnerId,
                                request.getProductName(), request.getProductPrice(),
                                request.getProductDescription());

                KindDto kindDto = new KindDto(
                                KindEntity.Default.getVirtualServiceKind().getKindName(),
                                KindEntity.Default.getVirtualServiceKind().getKindId());

                VirtualProductInformationDto dto = new VirtualProductInformationDto(
                                virtualProductDto, productDto, kindDto);

                return new VirtualProductInformationAdapter(dto);
        }
}
