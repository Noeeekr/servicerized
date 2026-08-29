package com.github.noeeekr.servicerized.identity.services.configuration;

import org.hibernate.Transaction;
import com.github.noeeekr.servicerized.identity.repository.dao.configuration.DaoConfiguration;
import lombok.Getter;

@Getter
public class ServiceConfiguration extends DaoConfiguration {
    private final Transaction tx;

    public ServiceConfiguration() {
        this.tx = null;
    };

    public ServiceConfiguration(Transaction tx) {
        this.tx = tx;
    };
}
