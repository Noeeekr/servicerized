package com.github.noeeekr.servicerized.product.service;

import org.springframework.boot.test.context.SpringBootTest;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateVirtualProductRequest;
import com.github.noeeekr.servicerized.product.repository.models.VirtualProductEntityTest;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.relations.VirtualProductInformation;
import com.github.noeeekr.servicerized.product.service.product.ProductService;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateVirtualProductCommandInterface;
import com.github.noeeekr.servicerized.response.Response;

@SpringBootTest
public class ProductServiceTest {
    public class Mock {
        public static Response<?> createVirtualProduct(ProductService productService,
                VirtualProductEntity virtualProduct) {
            CreateVirtualProductRequest request = new CreateVirtualProductRequest();
            request.setProductDescription(virtualProduct.getProduct().getProductDescription());
            request.setProductPrice(virtualProduct.getProduct().getProductPrice());
            request.setProductName(virtualProduct.getProduct().getProductName());

            request.setProvisionHours(virtualProduct.getProvisionHours());

            CreateVirtualProductCommandInterface command = CreateVirtualProductCommandInterface
                    .upgrade(request, UuidCreator.getTimeOrderedEpochFast());

            return productService.createVirtualProduct(command);
        }

        public static Response<VirtualProductInformation> createVirtualProduct(
                ProductService productService) {
            VirtualProductEntity virtualProduct = VirtualProductEntityTest.Mock.createSingle();

            CreateVirtualProductRequest request = new CreateVirtualProductRequest();
            request.setProductDescription(virtualProduct.getProduct().getProductDescription());
            request.setProductPrice(virtualProduct.getProduct().getProductPrice());
            request.setProductName(virtualProduct.getProduct().getProductName());
            request.setProvisionHours(virtualProduct.getProvisionHours());

            CreateVirtualProductCommandInterface command = CreateVirtualProductCommandInterface
                    .upgrade(request, UuidCreator.getTimeOrderedEpochFast());
            return productService.createVirtualProduct(command);
        }
    }
}
