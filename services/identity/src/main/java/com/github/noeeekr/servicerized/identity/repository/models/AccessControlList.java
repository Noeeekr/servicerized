package com.github.noeeekr.servicerized.identity.repository.models;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "access_control_list", schema = "identity",
        indexes = {@Index(name = "idx_acl_target_origin_perm",
                columnList = "target_group_id, origin_group_id, permission_id")},
        uniqueConstraints = {@UniqueConstraint(name = "uc_acl_target_origin_perm",
                columnNames = {"target_group_id", "origin_group_id", "permission_id"})})
public class AccessControlList {
    @ManyToOne
    @JoinColumn(name = "resource_kind_id",
            referencedColumnName = AclResource.COLUMN_RESOURCE_ID_NAME,
            foreignKey = @ForeignKey(name = "fk_resource_kind_id"), unique = false,
            nullable = false, updatable = false)
    private AclResource resource_kind_id;

    @ManyToOne
    @JoinColumn(name = "origin_group_id", referencedColumnName = Group.COLUMN_NAME_GROUP_ID,
            foreignKey = @ForeignKey(name = "fk_origin_group_id"), unique = false, nullable = false,
            updatable = false)
    private Group origin_group_id;

    @ManyToOne
    @JoinColumn(name = "target_group_id", referencedColumnName = Group.COLUMN_NAME_GROUP_ID,
            foreignKey = @ForeignKey(name = "fk_target_group_id"), unique = false, nullable = false,
            updatable = false)
    private Group target_group_id;

    @Id
    @ManyToOne
    @JoinColumn(name = "permission_id",
            referencedColumnName = AclPermission.COLUMN_PERMISSION_ID_NAME,
            foreignKey = @ForeignKey(name = "fk_permission_id"), unique = false, nullable = false,
            updatable = false)
    private AclPermission permission_id;

    @Column(name = "resource_id", unique = false, nullable = false, updatable = false)
    private UUID resource_id;
}
