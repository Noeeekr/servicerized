package com.github.noeeekr.servicerized.identity.repository.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EntityScan(basePackages = "com.github.noeeekr.servicerized.identity.repository.models")
@ActiveProfiles("schema")
class SchemaGeneratorTest {

    @Test
    void generateSchemaSql() {
        // Bootstraps JPA; outputs target/schema.sql during `mvn test` or `mvn package`
    }

}