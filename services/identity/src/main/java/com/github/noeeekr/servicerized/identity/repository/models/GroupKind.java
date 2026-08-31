package com.github.noeeekr.servicerized.identity.repository.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = GroupKind.TABLE_NAME, schema = "identity",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_group_kind_name", columnNames = {"group_kind_name"}),
                @UniqueConstraint(name = "uk_group_kind_id", columnNames = {"group_kind_id"})})
public class GroupKind {
    public static final String TABLE_NAME = "group_kinds";
    public static final String COLUMN_NAME_GROUP_KIND_ID = "group_kind_id";
    public static final String COLUMN_NAME_GROUP_KIND_NAME = "group_kind_name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GroupKind.COLUMN_NAME_GROUP_KIND_ID, nullable = false, updatable = false)
    private Long id;

    @Column(name = GroupKind.COLUMN_NAME_GROUP_KIND_NAME, nullable = false, updatable = false)
    private String name;
}
