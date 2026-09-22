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

All endpoints present in this micro-service follow the rules for developing endpoints defined at [Building REST API's](../development.md). The following endpoints are currently implemented and available:

**CREATE**:

- [Create product endpoint](./src/main/java/com/github/noeeekr/servicerized/product/controller/ProductController.java): Recieves a create product payload and creates a product. The controller assumes the product owner is the user whose authorization cookie belongs to. 

**POST**:

    None Available 
    
**PUT**:

    None Available 

**DELETE**:

    None Available 