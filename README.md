
# Welcome to the SMH Club Project
The purpose of the application is to manage the membership information for a club. <br>

Member information managed includes:

* Name, member number, birth date, joined date, year renewal is active
* Addresses (Work, Home, Other)
* Email addresses (Work, Personal, Other)
* Phone numbers (Work, Mobile, Other)
* Yearly renewal information (Renewal date)

The data is stored in a relation database and is served through a REST API.  A microservices architecture is used where authentication services are separate from the REST services.
The project is multi-module Gradle with centralized version management.

### Technologies and Frameworks
* Gradle build tool
* Spring Framework via Spring Boot starters
* PostgreSQL database for production
* H2 database for development
* Zonky embedded database for integration testing
* Flyway database migration
* Lombok boilerplate code generator
* Instancio random data generation for unit and integration testing
* Rest-assured REST client for integration testing

### Project Modules
#### club-api
A microservice that exposes REST CRUD endpoints.

### club-api-hateoas
A microservice that exposes the same endpoints as **club-api** but adds resource links to the data.

### club-data
Contains the domain entity objects as well as the related data repositories.

### club-shared
Contains code that is shared by other modules. 

### club-gateway
Entry point for the application.  Routes incoming calls to the appropriate microservice. <br>
_Not started._

### club-oauth2
Authentication microservice.<br>
_Under construction._

### build-logic
Provides centralized version management for Spring Boot, Spring Framework, and other the libraries used int he project.  
Can be used for other global tasks like static code analysis, documentation generation, etc.

### Future
Content services utilizing UI frameworks such as Angular or React.

## Reference Documentation

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.0/gradle-plugin)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.4.0/reference/htmlsingle/index.html#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.4.0/reference/htmlsingle/index.html#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.4.0/reference/htmlsingle/index.html#web.security)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/docs/3.4.0/reference/htmlsingle/index.html#web.security.oauth2.server)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.4.0/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.4.0/reference/htmlsingle/index.html#actuator)
* [PostgreSQL](https://www.postgresql.org/)
* [Lombok](https://projectlombok.org/)
* [Instancio](https://www.instancio.org/user-guide/)
* [Zonky Embedded Postgres](https://github.com/zonkyio/embedded-postgres)
* [Flyway](https://documentation.red-gate.com/flyway)
* [Rest-assured](https://github.com/rest-assured/rest-assured)

## Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
