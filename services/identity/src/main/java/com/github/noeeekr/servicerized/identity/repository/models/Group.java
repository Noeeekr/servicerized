package com.github.noeeekr.servicerized.identity.repository.models;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = Group.TABLE_NAME, schema = "identity")
public class Group {
        public static final String TABLE_NAME = "groups";
        public static final String COLUMN_NAME_GROUP_ID = "group_id";
        public static final String COLUMN_NAME_GROUP_NAME = "group_name";
        public static final String COLUMN_NAME_GROUP_PASSWORD = "group_password";

        @Id
        @GeneratedValue()
        @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
        @Column(name = Group.COLUMN_NAME_GROUP_ID, updatable = false, unique = true)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = GroupKind.TABLE_NAME,
                        referencedColumnName = GroupKind.COLUMN_NAME_GROUP_KIND_ID,
                        foreignKey = @ForeignKey(name = "fk_group_kind_id"), unique = false,
                        nullable = false)
        private GroupKind groupKindId;

        @Column(name = Group.COLUMN_NAME_GROUP_PASSWORD, updatable = true, unique = false,
                        nullable = false)
        private String password;

        @Column(name = Group.COLUMN_NAME_GROUP_NAME, updatable = true, unique = false,
                        nullable = false)
        private String name;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "group_owner_id", referencedColumnName = User.COLUMN_USER_ID_NAME,
                        foreignKey = @ForeignKey(name = "fk_group_owner_id"), nullable = false,
                        unique = false)
        private User owner;

        public Group setOwner(User owner) {
                this.owner = owner;
                return this;
        }

        public Group setGroupKindId(GroupKind knd) {
                this.groupKindId = knd;
                return this;
        }

        public Group setPassword(String password) {
                this.password = password;
                return this;
        }

        public Group setName(String name) {
                this.name = name;
                return this;
        }
}
