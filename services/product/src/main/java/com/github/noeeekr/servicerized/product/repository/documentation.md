# Documentation

## Introduction to Repository

Repository folder provides classes that handle remote database operations. Its subfolders are meant to provide utilities for consistency, simplicity and abstraction to these operations. 

```dto/```: _Data Transfer Object_ folder provides classes, reflecting database entities, for consistent communication between different parts of system and domains. They are meant for general use and do not implement any significant business rules, for this reason, guaranteeing that they contain only the expected data is developer's responsability. Such examples of this are ```Client Dtos``` that extended these classes and guarantee they are safe for serialization as JSON and ```Internal Dtos``` that are used for inter-subsystem communication.

```interfaces/```: _Interfaces_ folder provides interfaces that reflect the base entities for database, their relations and fields. They are meant to provide a consistency access behaviour for different levels of the database entities, relations and fields across codebase. Think about them as **Access Interfaces**. 

```migrations/```: _Migrations_ folder provides code migrations for entities, mainly used for population of default static rows of different entities.

```models/```: _Models_ folder contains classes that reflect and define the database entities for usage across service and on database manipulation.

```query/```: _Query_ folder contains helper classes that abstract some recorrent operations from _Repositories_ classes.