package com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields;

/**
 * @deprecated Currently, there is no need for this class, only {@link VirtualProductEntity} is
 *             available, which manages its inventory itself. Still, it is kept for future updates.
 */
public interface ProductInventoryFilterableFieldsInterface
                extends ProductInventoryFieldsInterface.Id {
        public boolean isEmpty();
}
