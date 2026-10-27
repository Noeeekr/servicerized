package com.github.noeeekr.servicerized.product.repository.models.query.update;

import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;

public class UpdateVirtualProductFields implements UpdateVirtualProductFieldsInterface {
    private int provisionHours;
    private int maxWeeklyProvisionHours;

    public UpdateVirtualProductFields(int provisionHours) {
        this.provisionHours = provisionHours;
    }

    public UpdateVirtualProductFields(UpdateVirtualProductFieldsInterface virtualProduct) {
        this.provisionHours = virtualProduct.getProvisionHours();
    }

    public int getMaxWeeklyProvisionHours() {
        return this.maxWeeklyProvisionHours;
    }

    public int getProvisionHours() {
        return this.provisionHours;
    }
}
