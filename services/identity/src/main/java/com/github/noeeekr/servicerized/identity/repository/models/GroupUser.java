package com.github.noeeekr.servicerized.identity.repository.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = GroupUser.TABLE_NAME, schema = "identity")
public class GroupUser {
    public static final String TABLE_NAME = "groups_users";

    @EmbeddedId
    @Column(name = "group_user_id", updatable = false, unique = true)
    private GroupUserId groupUserId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = User.COLUMN_USER_ID_NAME, foreignKey = @ForeignKey(name = "fk_user_id"))
    private User userId;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = Group.COLUMN_NAME_GROUP_ID, foreignKey = @ForeignKey(name = "fk_group_id"))
    private Group groupId;
}
