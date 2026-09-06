package com.github.noeeekr.servicerized.identity.repository.dto.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.identity.repository.models.Group;
import com.github.noeeekr.servicerized.identity.repository.models.GroupKind;
import com.github.noeeekr.servicerized.identity.repository.models.GroupKinds;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ClientGroupDtoUtils {
    @Autowired
    private final EntityManager entityManager;

    public static Group createPrimaryAccessGroup(EntityManager entityManager, User user,
            String password) {
        GroupKind groupKindReference =
                entityManager.getReference(GroupKind.class, GroupKinds.AccessGroup.getId());

        Group initialAccessGroup = new Group();
        initialAccessGroup.setPassword(password).setGroupKindId(groupKindReference).setOwner(user)
                .setName(user.getName());

        return initialAccessGroup;
    }

    public Group createPrimaryAccessGroup(User user, String password) {
        return ClientGroupDtoUtils.createPrimaryAccessGroup(entityManager, user, password);
    }
}
