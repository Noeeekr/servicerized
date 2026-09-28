# Documentation

## Folder Structure

```
    java/com/github/noeeekr/servicerized/product
        repository/         repository, models, their interfaces etc.
            ProductRepository.java      data mutation service for product.
            [EntityRepository].java     data mutation service.

            interfaces/     data mutation or evolution interfaces.
            models/         data entities for database.
            dto/            data transfer objects.

        controller/         controllers and their helper classes in sub-folders.
            response/       controller response classes.
            request/        controller requests classes.

        failures/
            ProductFailures.java   predictable failures, related to product model.
            [ModelFailures].java   predictable failures, related to this model.
            Failures.java          predictable failures, unrelated to any model.

        service/
            ProductService.java     product-related business logic. 
```
## Testing

    No tests available

## Services

All helper services present in this micro-service follow the rules for developing helper services defined at [Building Helper Services](../development.md). The following services are currently implemented and available:
-  [ProductService](./src/main/java/com/github/noeeekr/servicerized/product/service/ProductService.java): Implement different business operations for logic related to product data. Some of these are: operation payload validation, operation authorization and data mutation.

## Endpoints

For more information about default endpoint REST response, check [Default Endpoint Responses](../documentation.md#default-endpoint-responses).

All endpoints present in this micro-service follow the rules for developing endpoints defined at [Building REST API's](../development.md). The following endpoints are currently implemented and available:

#### Products

The current implementation for endpoints related to product operations can be found at [Product Endpoints](./src/main/java/com/github/noeeekr/servicerized/product/controller/ProductController.java).

- Create Endpoints: Contains endpoints related to creating products.
  - Create One: Registers a single service on the user's group.
```
    POST to /api/product
    Body: JSON 
        key: name           type: String (Max 256 Characters)
        key: description    type: String (Max 256 Characters)
        key: price          type: Integer (Only Positive)
        key: provisionHours type: Integer (Only Positive)

    Failure Causes:
        DOCUMENTATION_MISSING
        
    On success, replies with:
        Default Endpoint Response (success state) with a single product data as JSON:
            JSON
                key: id            type: UUID (Version 7)
                key: ownerId       type: UUID (Version 7)
                key: name          type: String (Max 256 Characters)
                key: description   type: String (Max 256 Characters)
                key: price         type: Integer (Only Positive)
```
- Listing Endpoints:
  - List one: List a single products.
```
    POST to /api/product/list
    Body: JSON
        key: filter                  type: Object
            key: productFilter       type: Object
                key: productId       type: UUID
                key: productName     type: String
            key: categoryFilter      type: Object
                key: categoryId      type: UUID
                key: categoryName    type: String

    Failure Causes:
        DOCUMENTATION_MISSING

    On success, replies with:
        Default Endpoint Response (success state) with a single product data as JSON:
            JSON
                key: id            type: UUID (Version 7)
                key: ownerId       type: UUID (Version 7)
                key: name          type: String (Max 256 Characters)
                key: description   type: String (Max 256 Characters)
                key: price         type: Integer (Only Positive)
```
  
#### Products (Categories)

- Attach:
```
    DOCUMENTATION_MISSING
```
- Dettach:
```
    DOCUMENTATION_MISSING
```