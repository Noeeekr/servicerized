package com.github.noeeekr.servicerized.identity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

public class IdentityServiceTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void contextStartup() {
        this.contextRunner.run(IdentityServiceTest::checkContextHealth);
    }

    // Helper functions
    public static boolean checkContextHealth(AssertableApplicationContext context) {
        return true;
    }
}