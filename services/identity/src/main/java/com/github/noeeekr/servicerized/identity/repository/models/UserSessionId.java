package com.github.noeeekr.servicerized.identity.repository.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionId implements Serializable {
    @Column(name = "session_group_id")
    private UUID groupId;

    @Column(name = "session_user_id")
    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserSessionId))
            return false;
        UserSessionId that = (UserSessionId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}
