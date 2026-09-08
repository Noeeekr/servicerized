
## Endpoints

Authentication controller provides the following endpoints:

### Signup 

This endpoint is located at [Authentication Controller](../src/main/java/com/github/noeeekr/servicerized/identity/controller/AuthenticationController.java)

- Description:
    This endpoint gets client data to create a user account with a authentication group. The account requires a unique username, unique e-mail and a password the client must provide.

    When the account is created successfully, the endpoint also creates a confirmation token, that is send to the e-mail providen by client.

- Usage:
    In order for user to sign-up, they must send a POST request to the endpoint located at [Authentication Controller](../src/main/java/com/github/noeeekr/servicerized/identity/controller/AuthenticationController.java)
(README.md) containing data defined in [Sign-up Endpoint Request](../src/main/java/com/github/noeeekr/servicerized/identity/controller/request/SignUpRequest.java).

    After that, if everything is done correctly on client side and server does the expected behaviour, an account confirmation link will be send to user's email. 

    **NOT IMPLEMENTED** If user does not confirm the account for 7 days, the account will be freed.


### Signup confirmation

This endpoint is located at [Authentication Controller](../src/main/java/com/github/noeeekr/servicerized/identity/controller/AuthenticationController.java)

- Description: 
    This endpoint validates the user e-mail is valid and allows access to [Sign-in Endpoint](../src/main/java/com/github/noeeekr/servicerized/identity/controller/AuthenticationController.java).

    When user reaches this endpoint, it means the e-mail is valid. Which then triggers the endpoint to find the authentication account related to the token providen, and then mark this account as confirmed.

- Usage:
    Communication through this endpoint requires a GET request with the query params defined at [Sign-up Confirmation Endpoint Data](../src/main/java/com/github/noeeekr/servicerized/identity/controller/AuthenticationController.java). It is expected for the query param token to be only obtainable through the method used in [Signup Endpoint](#signup).

### Signin

This endpoint is located at [Authentication Controller](../src/main/java/com/github/noeeekr/servicerized/identity/controller/AuthenticationController.java)

- Description:
    This endpoint allows client to create a authenticated session to the API. An authenticated session is needed for a series of features. 

- Usage:
    Communication through this endpoint requires a POST request with the data defined at [Sign-in Endpoint Request](../src/main/java/com/github/noeeekr/servicerized/identity/controller/request/SignInRequest.java). If the data matches a known access group, a JWT short-lived session is created for the client and returned as a http cookie. Otherwise, the client recieves an error response defined at [Client Response](../src/main/java/com/github/noeeekr/servicerized/identity/controller/response/ClientResponse.java)

    If the client request data targets a authentication account whose e-mail is not validated and, for some reason, doesn't contain any account confirmation links, the signing attempts to this endpoint will create a account confirmation link.

    If the client request data targets a authentication account whose e-mail is not validated and contains a pending account confirmation link, this endpoint will return an error response to client containing a description of the issue.