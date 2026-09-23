package com.github.noeeekr.servicerized.identity.repository.models;


import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import com.github.noeeekr.servicerized.identity.repository.dto.UserDto;
import com.github.noeeekr.servicerized.identity.repository.dto.client.ClientUserDto;
import com.github.noeeekr.servicerized.identity.repository.interfaces.CreateUserInterface;
import com.github.noeeekr.servicerized.identity.repository.interfaces.UserInterface;
import com.github.noeeekr.servicerized.repository.models.MetricsEntity;
import com.github.noeeekr.servicerized.response.client.ClientResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = User.TABLE_NAME, schema = "identity")
public class User extends MetricsEntity implements ClientResponseDto, UserInterface {
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

    /* Relations */

    @OneToMany(mappedBy = "owner")
    List<Group> groups;

    @OneToMany(mappedBy = "user")
    List<UserEmailConfirmation> confirmations;

    /* Static methods */

    public static User from(CreateUserInterface user) {
        return new User().setEmail(user.getUserEmail()).setName(user.getUserName());
    }

    /* Instance methods */

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public ClientUserDto prepareToClient() {
        return UserDto.getClientDto(this);
    }

    /* Getter */
    public String getUserName() {
        return this.name;
    }
    public String getUserEmail() {
        return this.email;
    }
    public UUID getUserId() {
        return this.id;
    }
}
