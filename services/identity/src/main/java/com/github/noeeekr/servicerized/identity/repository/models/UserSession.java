package com.github.noeeekr.servicerized.identity.repository.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
    @Column(name = "user_session_id")
    private UserSessionId UserSessionId;

}
