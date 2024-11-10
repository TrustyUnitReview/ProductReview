# Database Documentation

## Versioning
- Database Provider: PostrgreSQL
- Version: 16.4 

## Local Setup
- Install PostgreSQL 16.4 
- Create a local database using psql (or any other means)
  * Use Encoding UTF8
  * Use locale en_US.UTF-8
  * if there is a locale mismatch, use template0
- Connect springboot to database by modifying application.properties
  * Replace ${ENV_VAR} with local references 
  * Ex: *${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}* turns into *localhost:5432/postgres*

## CI/CD Pipeline
Previously, tests running in the pipeline used an embedded database, 
H2. Most likely, the GitHub runner would use its own main memory for the database. 
Switching to a server-based database poses the issue of which database server 
should be used for tests in the pipeline. It would be unideal to test against the production database server.

Solution: GitHub allows for the use of service containers in their workflows.
Meaning, a postgres image can be used to setup a temporary containerized database to test against.
The workflow can define environment variables which can be accessed by springboot's application.properties file.
It is expected that tests will be populating tables and querying data, which will be stored in the containerized database.
Therefore, tests ran locally will behave the same on the containerized database.

Containerized database: https://docs.github.com/en/actions/use-cases-and-examples/using-containerized-services/creating-postgresql-service-containers

## Deployed Application and Database Servers on Azure
As mentioned before, the H2 database most likely uses the application deployment server's main memory for the database.
Deploying a PostgreSQL database on Azure allows for the application to instead run against a deployed server. 
This allows for actual data persistence and greater control over the schema (more on this later). 

The deployed application is provided with the connection details of deployed database allowing for the communication.
This simply requires defining the environment variables in the application server for spring boot to use.

Externalized Configuration: https://docs.spring.io/spring-boot/reference/features/external-config.html

Springboot to PostgreSQL on Azure: https://learn.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-data-jdbc-with-azure-postgresql?tabs=passwordless%2Cservice-connector&pivots=postgresql-passwordless-flexible-server

## Database migrations

spring.jpa.hibernate.ddl-auto is a property which allows Hibernate to initialize the database.
Depending on the generation strategy, various different actions can occur.
The default generation strategy for embedded databases is 'create-drop' where the on startup, 
the application will initialize the database while on termination delete the database.

For server-based databases, the default setting for spring.jpa.hibernate.ddl-auto is none (meaning no initialization is done).
Currently, the spring.jpa.hibernate.ddl-auto is set to create-drop. 
However, this is not preferable and will be modified in a future issue.
For future steps, flyway will be used to setup the schema (create required relations) and spring.jpa.hibernate.ddl-auto will be set to none.
Springboot provides support for migrations using flyway which will ensure on startup, that the database matches the expected schema. 
Note: This approach needs to be discussed and coordinated with other team members to ensures all entities are properly initialized.

Database Initialization: https://docs.spring.io/spring-boot/how-to/data-initialization.html
