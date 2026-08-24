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
@Table(name = "users", schema = "identity")
public class User extends Metrics {
    public static final String COLUMN_USER_ID_NAME = "user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = User.COLUMN_USER_ID_NAME, updatable = false, unique = true, nullable = false)
    private UUID userId;

    @Column(name = "user_name", updatable = true, unique = true, nullable = false)
    private String userName;

    @Column(name = "user_email", updatable = false, unique = true, nullable = false)
    private String userEmail;

    @Column(name = "user_password", updatable = true, unique = false, nullable = false)
    private String userPassword;
}
