package com.github.noeeekr.servicerized.identity.repository.models;


import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = User.TABLE_NAME, schema = "identity")
public class User extends Metrics implements ClientResponseDto {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_USER_ID_NAME = "user_id";
    public static final String COLUMN_USER_EMAIL_NAME = "user_email";

    @Id
    @GeneratedValue()
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = User.COLUMN_USER_ID_NAME, updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "user_name", updatable = true, unique = true, nullable = false)
    private String name;

    @Column(name = "user_email", updatable = false, unique = true, nullable = false)
    private String email;

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Object prepareToClient() {
        return this;
    }
}
