package com.github.noeeekr.servicerized.identity.repository.models;

import com.github.noeeekr.servicerized.repository.models.Metrics;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_sessions", schema = "identity")
public class UserSession extends Metrics {

    @EmbeddedId
    private UserSessionId id = new UserSessionId();

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "session_group_id", foreignKey = @ForeignKey(name = "fk_session_group_id"))
    private Group group;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "session_user_id", foreignKey = @ForeignKey(name = "fk_session_user_id"))
    private User user;

}
