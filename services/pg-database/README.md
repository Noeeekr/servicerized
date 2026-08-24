# pg-database defines database configurations, migrations etc

1. [Managing the database](managing-the-database)
   1. Development
      1. [Preparing the development database](#preparing-the-development-database)
      2. [Creating the development database](#creating-the-development-database)
   2. Production
      1. [Preparing the production database](#preparing-the-production-database)
      2. [Creating the production database](#creating-the-production-database)
      
2. Migrations
   1. [Naming conventions](#naming-conventions)
    2. [Migrating to development](#migrating-to-development)
    3. [Migrating to production](#migrating-to-production)

<br/>
<br/>
<br/>

## Managing the database

The database is created using docker images. All migrations are done through flyway. 

<br/>
<br/>

## Configurating the development database

Requirements:
- Docker >= 29.7.2

The postgres database is created through docker images. To create a database through a docker image, some configuration on [compose.yml](../../compose.yml) is necessary.

The following environment variables need to be set:

- POSTGRES_USER: The name of the database owner user.
- POSTGRES_PASSWORD: The password of the database owner user.
- POSTGRES_DB: The name of the database to be created.

#### Locating database data

Also, the image needs to know where to store the database data. This information can be configured by creating a volume in [compose.yml](../../compose.yml) that routes /var/lib/postgresql/data to a desired location/volume.

#### Listening to ports

Additionally, the database will listen all the port mappings defined in [compose.yml](../../compose.yml)

```
services:
  pg-database:
    ...
    ports:
      - "5432:5432"
```

<br/>

## Creating the development database


1. Configure postgres database ([database configurations](#preparing-the-development-database)) 
2. Run the following command:
```
$ docker compose up -d pg-database
```

<br/>
<br/>
<br/>
<br/>
<br/>

## Configurating the production database


Requirements:
- Docker >= 29.7.2

The postgres database is created through docker images. To create a database through a docker image, some configuration on [compose.yml](../../compose.yml) is necessary.

The following environment variables need to be set:

- POSTGRES_USER: The name of the database owner user.
- POSTGRES_PASSWORD: The password of the database owner user.
- POSTGRES_DB: The name of the database to be created.

#### Locating database data

Also, the image needs to know where to store the database data. This information can be configured by creating a volume in [compose.yml](../../compose.yml) that routes /var/lib/postgresql/data to a desired location/volume.

#### Listening to ports

Additionally, the database will listen all the port mappings defined in [compose.yml](../../compose.yml)

```
services:
  pg-database:
    ...
    ports:
      - "5432:5432"
```

## Creating the production database

<br/>
<br/>
<br/>

## Migrations

<br>

## Creating a migration

#### Configuring flyway connection 

Before running migrations, flyway needs a configuration file to inform how it will handle the database. The path to the configuration file can be found [here](./pom.xml), on the following section:
```
...
<configurationFiles>
    <configurationFile>example/path/file.conf</configurationFile>
</configurationFiles>
...
```
The configuration file needs to contain the following variables:
```
flyway.url=jdbc:[postgres url]
flyway.user=[postgres user]
flyway.password=[postgres password]
flyway.driver=org.postgresql.Driver
```

* Replaces the terms between [] with the actual configuration (from [managing database](#managing-the-database))

#### Configuring migrations locations 

Additionally, flyway needs to know the path to the files containing the migration steps it will perform. The path to the location configuration can be found [here](./pom.xml), on the following section:
```
...
<locations>
    <location>example/path/folder</location>
</locations>
...
```
      

#### Naming conventions

When creating a migration file, this is the recommended naming format.

```
[Prefix][Version]__[Description].[Ext]
[Prefix][Version]__[Description].[Ext]
```

<br>

## Migrating to production

__Make sure to follow migration rules__

Requirements:
- All migration files up to the latest (or the desired version).
- A flyway executable.

1. Do the steps at [creating the production database](#creating-the-production-database) 
2. Upload the migration file and the flyway executable to a machine with access to the database.
3. Run the following command:

```
$ flyway migrate -configFiles=/my/file/path/latest.conf migrate 
```

* Replace /my/file/path/latest.conf with the path to the latest migration file.

<br>

## Migrating to development 

1. Do the steps at [creating the development database](#creating-the-development-database)
2. Run:
``` 
$ mvn flyway:migrate
```

<br>
<br>

### General Notes

* On creation, the host of the database will be defined as the container name (pg-database).