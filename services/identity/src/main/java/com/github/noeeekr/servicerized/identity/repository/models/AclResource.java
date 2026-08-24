package com.github.noeeekr.servicerized.identity.repository.models;

import java.util.UUID;
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
@Table(name = AclResource.TABLE_NAME, schema = "identity")
class AclResource {
    public static final String TABLE_NAME = "acl_resources";
    public static final String COLUMN_RESOURCE_ID_NAME = "resource_id";

    @Id()
    @Column(name = AclResource.COLUMN_RESOURCE_ID_NAME)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID resource_id;

    @Column(name = "resource_name", nullable = false, unique = true, length = 128)
    private String resource_name;
}
