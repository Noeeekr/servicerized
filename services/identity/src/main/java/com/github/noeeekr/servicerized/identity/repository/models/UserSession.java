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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_sessions", schema = "identity")
public class UserSession extends Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_session_id")
    private UUID UserSessionId;

}
