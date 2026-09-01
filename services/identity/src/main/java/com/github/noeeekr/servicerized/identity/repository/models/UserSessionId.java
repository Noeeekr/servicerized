package com.github.noeeekr.servicerized.identity.repository.models;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "session_group_id", foreignKey = @ForeignKey(name = "fk_session_group_id"))
    private UUID groupId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "session_user_id", foreignKey = @ForeignKey(name = "fk_session_user_id"))
    private UUID userId;
}
