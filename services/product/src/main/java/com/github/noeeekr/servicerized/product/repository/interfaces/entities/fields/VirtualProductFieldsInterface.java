package com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields;

public interface VirtualProductFieldsInterface {
    public interface ProvisionHours {
        public int getProvisionHours();
    }
    public interface MaxWeeklyProvisionHours {
        public int getMaxWeeklyProvisionHours();
    }

    /**
     * NullableProvisionHours is a nullable version of {@link ProvisionHours} since the field can be
     * null before database insertion.
     */
    public interface NullableProvisionHours {
        public Integer getProvisionHours();
    }

    /**
     * NullableMaxWeeklyProvisionHours is a nullable version of {@link MaxWeeklyProvisionHours}
     * since the field can be null before database insertion.
     */
    public interface NullableMaxWeeklyProvisionHours {
        public Integer getMaxWeeklyProvisionHours();
    }

    public interface Id extends ProductFieldsInterface.Id {

    }
}
