# Service Development Documentation

[1. Introduction](#introduction)<br/>
[2. Building REST API's](#building-rest-apis)<br/>
[3. Naming Conventions](#naming-conventions)

## Introduction

These are instructions to follow when creating a new service. The instructions contains design patterns that must be followed.

## Helper Features

### Helpers for Developing Controllers

While building controller, if the helpers services communicate through [Common Package Responses](../common/response/pom.xml) instead of custom-defined responses for inter-class communication, it is possible to use simplified abstractions for handling controller responses. The package [Common Package Controller](../common/controller/pom.xml) offers controller helper utilities for handling responses that comes from the common response package. 

## Rules

### Building Controllers

The following suggestions should be implemented and checked for every controller:
- **Input Validation**: All data should be validated to check if it follows the right format. Additionally, it should be validated to guarantee its values are coherent with the relations they target. For example, and ID that points to a user product must be validated to guarantee it is actually pointing for a product of that user.

### Building Helper Services

Please, for consistency across services, prefer the usage of [Common Package Responses](../common/response/pom.xml) instead of custom-defined responses for inter-class communication as much as possible. This package provides features such as predictable response objects and rich failure handling.

### Building REST API's

Please, prefer the usage of HTTP Methods for routing than endpoing naming conventions when possible.

Example:

Prefer:

```
GET product:        For reading products
PUT product:        For replacing products
POST product:       For creating products
PATCH product:      For updating products
DELETE product:     For deleting products
```

Over:

```
POST product:        For reading products
POST product/replace:        For replacing products
POST product/create:       For creating products
POST product/update:      For updating products
POST product/delete:     For deleting products
```

Or any other ways.

### Naming Conventions

Please, prefer the usage of pascal case over other conventions for objects.

Example:
- UserDao instead of UserDAO.
- GroupDao instead of Group_Dao.
- SubjectDao instead of SUBJECT_DAO.
  
Please, prefer the usage of full length names over abbreviations.

Example:
- UserObjectDao instead of Uod.
- UsernameAndPasswordToken instead of Upt.
- MyLongPhraseThatDoesntMatter instead of Mlptdm.

Please, prefer the usage of uppercase instead of pascal case for primitive constants.
Example
- USER_ROLE_ID insteado f UserRoleId.
- OWNER_ROLE_ID instead of OwnerRoleId.
- REVIEWER_ROLE_ID instead of ReviewerRoleId.

### Models

The following are naming conventions for how data is handled by services based on where its data comes from, please read them carefully:

- ```[Operation Kind] [Model] Request```: The most basic model naming convention, it defines the untrustable data that comes from client.
- ```[Operation Kind] [Model]```: The data from previous step, but now is validated by API trustable sources, like database, cookies etc. It can also contain fields that the API deemed necessary.
- ```[Model]```: The target data after being created, recovered, modified etc.

Examples:

- ```Create User Request```: The necessary data client requires to pass to create a user account.<br>
  ```Username: string, Password: string, Email: string.``` 
- ```Create User```:: The same data after being validated. Additionally, service may add/modify new fields to it.<br>
  ```Username: is unique, Password: is long enough, Email: is unique.```
  ```CreatedAat: added, DeletedAt: added.```
- ```User```: The data created, modified, recovered etc.
  