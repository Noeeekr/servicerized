# Documentation

## Introduction to Repository Queries

The code inside this folder serves the purpose of abstracting the creation of queries from the central repository folder. Its intended practical purpose is to be extended/implemented by repository classes, adding utilitary features to it, reducing code pollution & local complexity. 

To facilitate usage, classes on this package follow a strict naming convention, a list of available naming structures and their purposes is available here:

- EntityRepositoryQueryBuilder: Builds queries for listing, updating, deleting and creating the entity.
- EntityRepositoryQueryFilterBuilder: Builds query filters for target specific entities on delete, update, list and creation operations.

#### Example:
```
ProductRepositoryQueryBuilder.java // Handle query creation for product entity and its relations.

ProductRepositoryQueryFilterBuilder.java // Handle query filter creation for product entity and its relations. 
```

## Instructions for Developing Repository Queries 

Please follow the follow naming convention while building for this folder structure:

- Create only filters & query builders. For further consideration please refer personally to the current manager of the project.
- Name them accordinly to [This example](#example).
- All interfaces created on this package that are meant to be implemented by a repository must implement RepositoryHelperInterface.java or a class that implements it.
- Filter interfaces should be local.