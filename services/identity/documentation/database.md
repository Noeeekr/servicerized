## Database

This service contains database models that are managed JPA entities.

### Migrations

This service can migrate its own database entities, that are separated in two kinds of migrations. The first kind of migrations are pure SQL migration files, that can be found at [SQL Migrations Folder](../src/main/resources/migration). The second kind of migrations are dynamically populated data for default system fields, that can be foiund at [Java Dynamic Migrations Folder](../src/main/java/com/github/noeeekr/servicerized/identity/repository/migrations).

#### Creating Migrations

Additionaly, this service offers helpers for creating *.sql* migration files. One of these helpers is located at [SQL Migration Generator](../src/test/java/com/github/noeeekr/servicerized/identity/repository/models/SchemaGeneratorTest.java), it contains a single test that, when run, generates a sql migration schema at [Sql Migration Generator Output](../target).

In order to use it, it requires maven updated and the packages described in pom installed. For this, go to this service root folder and run:
```
    $ mvn install -DskipTests
```

After that, still on this service root folder, run:
```
    $ mvn clean package -Dtest=SchemaGeneratorTest
```

It may fail if it cannot infer the types of models as postgres sql version, otherwise it always passes, on execution it creates files at [Target Folder](../target) with the names defined at [SQL Migration Configuration](../src/test/resources/application-generate-sql.yml)


#### Running Migrations

It is crucial to understand that this service depends on [Database Service](../../pg-database) for running migrations. This service is the actual responsible for looking for the migrations defined at this service and resolving them. It is preffered to check its documentation before attempting to do so.    