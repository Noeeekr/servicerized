testing profiles available:
    in-memory-db: sets h2 in memory storage
    generate-sql: sets a test that generates sql from jakarta entities model
    local-mailer: sets a local mailing server and defines its configurations
        also, it provides under the following parameters the configurations
            Username: @Value("{spring.mail.username}")
            Password: @Value("{spring.mail.password}")
            others available can be seen at [NOT IMPLEMENTED](./)