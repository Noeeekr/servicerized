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
        public static final String COLUMN_GROUP_ID_NAME = "group_id";

        @Id
        @GeneratedValue()
        @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
        @Column(name = Group.COLUMN_GROUP_ID_NAME, updatable = false, unique = true)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = GroupKind.TABLE_NAME,
                        referencedColumnName = GroupKind.COLUMN_NAME_GROUP_KIND_ID,
                        foreignKey = @ForeignKey(name = "fk_group_kind_id"), unique = false,
                        nullable = false)
        private GroupKind groupKindId;

        @Column(name = "group_password", updatable = true, unique = false, nullable = false)
        private String password;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "group_owner_id", referencedColumnName = User.COLUMN_USER_ID_NAME,
                        foreignKey = @ForeignKey(name = "fk_group_owner_id"), nullable = false,
                        unique = false)
        private User ownerId;

        public Group setGroupKindId(GroupKind knd) {
                this.groupKindId = knd;
                return this;
        }

        public Group setPassword(String password) {
                this.password = password;
                return this;
        }
}
