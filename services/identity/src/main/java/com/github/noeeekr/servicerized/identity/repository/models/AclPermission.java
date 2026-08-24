package com.github.noeeekr.servicerized.identity.repository.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = AclPermission.TABLE_NAME, schema = "identity")
class AclPermission {
    public static final String TABLE_NAME = "acl_permissions";
    public static final String COLUMN_PERMISSION_ID_NAME = "permission_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AclPermission.COLUMN_PERMISSION_ID_NAME, unique = true, nullable = false,
            updatable = false)
    private Long permissionId;

    @Column(name = "permission_name", unique = true, nullable = false, updatable = false)
    private String permissionName;
}
