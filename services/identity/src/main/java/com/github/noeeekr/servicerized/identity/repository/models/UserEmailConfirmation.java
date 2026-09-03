package com.github.noeeekr.servicerized.identity.repository.models;

import java.util.UUID;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
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
public class UserEmailConfirmation extends Metrics {
    public static final String TABLE_NAME = "users_email_confirmations";

    public static final String COLUMN_NAME_TOKEN = "token";
    public static final String COLUMN_NAME_CONFIRMED = "confirmed";

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_email_confirmation_user_id"))
    private User user;

    @Id
    @GeneratedValue()
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "confirmation_token", nullable = false)
    private UUID token;

    @Column(name = "confirmed", nullable = false)
    @ColumnDefault(value = "FALSE")
    private boolean confirmed;

    public UserEmailConfirmation setUser(User user) {
        this.user = user;
        return this;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Entity %s:", UserEmailConfirmation.class.getName()));
        builder.append(String.format("\n\tConfirmed: %b", this.confirmed));
        builder.append(String.format("\n\tToken UUID: %s", this.token));
        builder.append(String.format("\n\tTarget User UUID: %s", this.user.getId()));
        return builder.toString();
    }
}
