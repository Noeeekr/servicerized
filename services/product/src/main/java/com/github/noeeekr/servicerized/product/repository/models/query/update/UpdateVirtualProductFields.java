package com.github.noeeekr.servicerized.product.repository.models.query.update;

import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;

public class UpdateVirtualProductFields implements UpdateVirtualProductFieldsInterface {
    private Integer provisionHours;
    private Integer maxWeeklyProvisionHours;

    public UpdateVirtualProductFields(int provisionHours) {
        this.provisionHours = provisionHours;
    }

    public UpdateVirtualProductFields(UpdateVirtualProductFieldsInterface virtualProduct) {
        this.provisionHours = virtualProduct.getProvisionHours();
    }

    public Integer getMaxWeeklyProvisionHours() {
        return this.maxWeeklyProvisionHours;
    }

    public Integer getProvisionHours() {
        return this.provisionHours;
    }
}
