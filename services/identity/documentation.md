# Documentation

## Migrations

This service migrations are available at [NOT IMPLEMENTED](README.md). The models are managed JPA entities. This offers some benefits, such as helpers for creating *.sql* migration files, one of them is a sql schema generation from entity inference. 

In order to use it, it requires maven updated and the packages described in pom installed. For this, go to this service root folder and run:
```
    $ mvn install -DskipTests
```

After that, still on this service root folder, run:
```
    $ mvn clean package -Dtest=SchemaGeneratorTest
```

It will generate the base sql files used to created and update the SQL migrations. [SQL Migration Generator](./src/test/java/com/github/noeeekr/servicerized/identity/repository/models/SchemaGeneratorTest.java) is a test that fails if it cannot infer the types of models as postgres sql version, otherwise it always passes, on execution it creates files at [Target Folder](./target) with the names defined at [SQL Migration Configuration](./src/test/resources/application-generate-sql.yml)

## Endpoints

Authentication controller provides the following endpoints:

### Signup 

This endpoint is located at [NOT IMPLEMENTED](README.md)

- Description:
    This endpoint gets client data to create a user account with a authentication group. The account requires a unique username, unique e-mail and a password the client must provide.

    When the account is created successfully, the endpoint also creates a confirmation token, that is send to the e-mail providen by client.

- Usage:
    In order for user to sign-up, they must send a POST request to the endpoint located at [NOT IMPLEMENTED](README.md) containing data defined in [NOT IMPLEMENTED](README.md).

    After that, if everything is done correctly on client side and server does the expected behaviour, an account confirmation link will be send to user's email. 

    **NOT IMPLEMENTED** If user does not confirm the account for 7 days, the account will be freed.


### Signup confirmation

This endpoint is located at [NOT IMPLEMENTED](README.md)

- Description: 
    This endpoint validates the user e-mail is valid and allows access to [Signin Endpoint](#signin) 

    When user reaches this endpoint, it means the e-mail is valid. Which then triggers the endpoint to find the authentication account related to the token providen, and then mark this account as confirmed.

- Usage:
    Communication through this endpoint requires a GET request with the data defined at [Signup Endpoint](#signup). It is expected for the token to be only obtainable through the method used in [Signup Endpoint](#signup).

### Signin
This endpoint is located at [NOT IMPLEMENTED](README.md)

- Description:
    This endpoint allows client to create a authenticated session to the API. An authenticated session is needed for a series of features. 

- Usage:
    **NOT IMPLEMENTED** Communication through this endpoint requires a POST request with the data defined at [NOT IMPLEMENTED](README.md). If the data matches a known access group, a JWT short-lived session is created for the client and returned as a http cookie. Otherwise, the client recieves an error response defined at [NOT IMPLEMENTED](README.md)

    **NOT IMPLEMENTED** If the client request data targets a authentication account whose e-mail is not validated and, for some reason, doesn't contain any account confirmation links, the signing attempts to this endpoint will create a account confirmation link.

    **NOT IMPLEMENTED** If the client request data targets a authentication account whose e-mail is not validated and contains a pending account confirmation link, this endpoint will return an error response to client containing a description of the issue.