package com.github.noeeekr.servicerized.product.repository.models;

import java.util.Random;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;

public class VirtualProductEntityTest {
    public class Mock {
        public static VirtualProductEntity createSingle(ProductEntity product) {
            Integer provisionHours =
                    new Random().nextInt(VirtualProductEntity.Domain.MIN_ACCEPTABLE_PROVISION_HOURS,
                            VirtualProductEntity.Domain.MAX_ACCEPTABLE_PROVISION_HOURS);
            return new VirtualProductEntity(product, provisionHours);
        }

        public static VirtualProductEntity createSingle() {
            ProductEntity product = ProductEntityTest.Mock.createSingle();

            return VirtualProductEntityTest.Mock.createSingle(product);
        }
    }
}
