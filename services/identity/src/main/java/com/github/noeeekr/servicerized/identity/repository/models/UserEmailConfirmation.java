package com.github.noeeekr.servicerized.identity.repository.models;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = UserEmailConfirmation.TABLE_NAME, schema = "identity")
public class UserEmailConfirmation {
    public static final String TABLE_NAME = "users_email_confirmations";
    
    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @Id
    @Column(name = "confirmation_token")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token;

    public UserEmailConfirmation setUser(User user) {
        this.user = user;
        return this;
    }
}