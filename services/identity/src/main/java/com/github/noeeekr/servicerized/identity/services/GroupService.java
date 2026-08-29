package com.github.noeeekr.servicerized.identity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.repository.dao.GroupDao;
import com.github.noeeekr.servicerized.identity.repository.models.Group;
import com.github.noeeekr.servicerized.identity.services.configuration.ServiceConfiguration;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {
    @Autowired
    private GroupDao dao;

    public Response<Group> createAccessGroup(Group group, ServiceConfiguration configuration) {
        Response<Group> response = dao.saveOne(group, configuration);
        // if (response.isSuccess() != true) {
        // return response;
        // }

        return response;
    }
}
