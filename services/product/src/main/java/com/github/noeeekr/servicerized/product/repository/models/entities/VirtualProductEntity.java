package com.github.noeeekr.servicerized.product.repository.models.entities;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.failures.ServiceFailures;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.VirtualProductInterface;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * VirtualProduct contains specific data related to the service kind for a generic product entity.
 */
@Getter
@Entity
@Table(name = VirtualProductEntity.METADATA.TABLE_NAME)
public class VirtualProductEntity implements VirtualProductInterface {
    public static final class METADATA {
        public static final String TABLE_NAME = "services";

        public static final String COLUMN_NAME_MAX_WEEKLY_PROVISION_HOURS =
                "maxWeeklyProvisionHours";
        public static final String COLUMN_NAME_PROVISION_HOURS = "provisionHours";
        public static final String COLUMN_NAME_PRODUCT_ID = "productId";

        public static final String DATABASE_COLUMN_NAME_MAX_WEEKLY_PROVISION_HOURS =
                "max_weekly_provision_hours";
        public static final String DATABASE_COLUMN_NAME_PROVISION_HOURS = "provision_hours";
        public static final String DATABASE_COLUMN_NAME_PRODUCT_ID = "product_id";
    }

    /**
     * class Domain contains business rules as constants and verification methods.
     */
    public static final class Domain {
        public static final int MAX_ACCEPTABLE_PROVISION_HOURS = 40;
        public static final int MIN_ACCEPTABLE_PROVISION_HOURS = 0;

        public static @Nullable String isMaxWeeklyProvisionHoursValid(
                Integer maxWeeklyProvisionHours) {
            if (maxWeeklyProvisionHours > VirtualProductEntity.Domain.MAX_ACCEPTABLE_PROVISION_HOURS) {
                return "o campo 'tempo máximo de provisão semanal' requer um valor <= 40 ao envés de 'tempo máximo de provisão semanal' > 40";
            }
            return null;
        }

        public static @Nullable String isProvisionHoursValid(Integer provisionHours) {
            if (provisionHours > VirtualProductEntity.Domain.MAX_ACCEPTABLE_PROVISION_HOURS) {
                return "o campo 'tempo de provisão' requer um valor <= 40 ao envés de 'tempo de provisão' > 40";
            }
            return null;
        }
    }

    @Id
    @Column(name = VirtualProductEntity.METADATA.DATABASE_COLUMN_NAME_PRODUCT_ID)
    private UUID productId;

    /**
     * Defines the amount of time the service provider believes this service takes to be completed
     * from the moment it starts.
     */
    @Column(name = VirtualProductEntity.METADATA.DATABASE_COLUMN_NAME_MAX_WEEKLY_PROVISION_HOURS,
            nullable = false)
    private int maxWeeklyProvisionHours;
    @Column(name = VirtualProductEntity.METADATA.DATABASE_COLUMN_NAME_PROVISION_HOURS,
            nullable = false)
    private int provisionHours;

    /**
     * The product this entity data increments.
     */
    @MapsId(VirtualProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID)
    @OneToOne
    @JoinColumn(name = ProductEntity.METADATA.DATABASE_COLUMN_NAME_PRODUCT_ID,
            foreignKey = @ForeignKey(name = "fk_virtual_product_target_product_id"))
    private ProductEntity product;

    public VirtualProductEntity(ProductEntity product, int provisionHours) {
        this.provisionHours = provisionHours;

        this.productId = product.getProductId();
        this.product = product;
    }

    public Response<?> isValid() {
        return VirtualProductEntity.isValid(this);
    }

    public static Response<?> isValid(VirtualProductEntity virtualProduct) {
        Response<?> response;

        response = VirtualProductEntity
                .isMaxWeeklyProvisionHoursValid(virtualProduct.maxWeeklyProvisionHours);
        if (!response.isSuccess()) {
            return response;
        }

        response = VirtualProductEntity.isProvisionHoursValid(virtualProduct.provisionHours);
        if (!response.isSuccess()) {
            return response;
        }

        return Response.success(virtualProduct);
    }

    public static Response<?> isMaxWeeklyProvisionHoursValid(Integer maxWeeklyProvisionHours) {
        String errorMessage =
                VirtualProductEntity.Domain.isMaxWeeklyProvisionHoursValid(maxWeeklyProvisionHours);
        if (errorMessage != null) {
            return Response.fromFailure(new ServiceFailures.BadRequest(errorMessage));
        }

        return Response.success(null);
    }

    public static Response<?> isProvisionHoursValid(Integer provisionHours) {
        String errorMessage = VirtualProductEntity.Domain.isProvisionHoursValid(provisionHours);
        if (errorMessage != null) {
            return Response.fromFailure(new ServiceFailures.BadRequest(errorMessage));
        }
        return Response.success(null);
    }
}
