package com.github.noeeekr.servicerized.identity.services.configuration;

import com.github.noeeekr.servicerized.identity.repository.dao.configuration.DaoConfiguration;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ServiceConfiguration extends DaoConfiguration {

    public ServiceConfiguration() {
        super();
    }
    
}
