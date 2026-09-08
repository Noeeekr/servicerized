## Testing

This service contains tests located at [Testing Folder](../src/test/java/com/github/noeeekr/servicerized/identity/). 

### Configuration

This service provides many configurations for tests, including profiles located at [Profiles Configuration](../src/test/resources/).

The following profiles are available:
- in-memory-db: sets a exclusive h2 in-memory storage and creates different connections for each context.
- generate-sql: sets a test that validates & generates sql from jakarta entities model.
- local-mailer: sets a local mailing server and defines its configurations, which also provides under the following parameters the configurations:
    - Username:  ``` @Value("{spring.mail.username}") ```
    - Password: ``` @Value("{spring.mail.password}") ```
    - others available can be seen at [Mailer Profile Configuration](../src/test/resources/application-local-mailer.yml).