# Services General Documentation

For instructions about creating services, check [Creating a service.](./development.md)

## Default Endpoint Responses

The default REST response for a success or an error is JSON object that reflects  _Spring Boot Framework_ [_ResponseEntity_](https://docs.spring.io/spring-framework/docs/7.0.x/javadoc-api/org/springframework/http/ResponseEntity.html) object containing a [_ClientResponse_](../common/response/src/main/java/com/github/noeeekr/servicerized/response/client/ClientResponse.java) object as body.

#### Default Error Response

On error state, the client response provides a key named "error" containing an error object. The data available inside the error object is:
```
"error": {
    "description": Description of the error.       // String
    "clientError": Defines if the error is client's fault, true if it is, false otherwise.  // Boolean
}
```

More information about the implementation of this error object can be found at [_ClientResponseError_](../common/response/src/main/java/com/github/noeeekr/servicerized/response/client/ClientResponseError.java).

#### Default Success Response

On success state, the client response provides a key named "payload" containing the target data. The data available can vary based on business logic.