# Services General Documentation

For instructions about creating services, check [Creating a service.](./development.md)

## Default Folders Across Services

- _Controller Folder_: Contains controller implementation, fine grain documentation and other features related to controller execution flow.
  - _Controller Model Folder_: Contains the format of data that the controller uses in external interactions, commonly named requests (usually, contains data that informs how the client views the API, such as requests and responses)

- _Service Folder_: Contains service implementation (as subfolders), fine grain documentation and other features related to service execution flow.
  - _Service Model Folder_: Contains the format of data that the services expectes to recieve in internal interactions, commonly named commands (preferably not as requests, to lower confusion with controller models).

- _Repository_: Contains implementation on how models are reflected on database.
  - _Repository Interfaces_: Contains general and specific interfaces that limits/defines the allowed behaviour for a series of interactions with repository entities.
    - _Repository Interface Fields_: Behaviour for specific model fields (getters, setters, updaters, subscribers etc). Also, used to guarantee methods naming and returning consistency across different service implementations in the codebase.
    - _Repository Interface Filters_: Contains interfaces that define which fields should be used for filtering across services. Mainly used to guarantee sorting consistency and equal filtering availability across codebase.
  
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