package com.github.noeeekr.servicerized.identity.services;

import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.UserFailure;
import com.github.noeeekr.servicerized.identity.repository.dao.UserDao;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserService {
    private UserDao dao;

    /**
     * @param user The object to be created
     * @return A Response object containing the created user on success. Otherwise, a Failure object
     *         that may contain the following: UnhandledException (exception on credentials
     *         validation), ResourceFound (credentials already taken), FailedPersit (exception on
     *         while creating user)
     */
    public Response<User> newUser(User user) {
        // Validation: User with this credentials must not exist.
        Response<User> response = dao.getByEmail(user.getEmail());
        if (response.isSuccess() != true) {
            return response;
        }
        User target = response.getPayload();
        if (target != null) {
            return Response.<User>newInstance()
                    .fail(new UserFailure.ResourceFound(String.format("table %s", User.TABLE_NAME),
                            String.format("user (id: %s)", target.getId().toString())))
                    .build();
        }

        // Persist: Create a user with this credentials.
        response = dao.saveOne(user);
        if (response.isSuccess() != true) {
            return response;
        }

        return Response.<User>newInstance().success(user).build();
    }
}
