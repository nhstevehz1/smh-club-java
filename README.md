
# Welcome to the SMH Club Project
The purpose of the application is to manage the membership information for a social club. <br>

Member information managed includes:

* Name, member number, birth date, joined date
* Addresses (Work, Home, Other)
* Email addresses (Work, Personal, Other)
* Phone numbers (Work, Mobile, Other)
* Yearly renewal information (Renewal date, renewal year)

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
* Angular Framework
* Angular Material Framework
* Angular Test Framework
* angular-oauth2-oidc for OpenId support.  Authored by Manfred Steyer

### Project Modules
#### club-api
A microservice that exposes REST CRUD endpoints.  
It supports OAuth2 token verification through an external OAuth2 server. 

### club-api-hateoas
A microservice that exposes the same endpoints as **club-api** but adds resource links to the data.
It supports OAuth2 token verification through an external OAuth2 server.

### club-frontend-angular
A single page application UI that represents the data served by the club-api.
Login is achieved through OIDC "code flow with PKCE".

### club-gateway
Entry point for the application.  Routes incoming calls to the appropriate microservice. <br>
_Not started._

### club-oauth2
Authentication microservice.<br>
An JPA implementation of the Spring Authentication Server.  This service isn't very useful without a UI.  
It is recommended that an off the shelf solution be used instead.

### build-logic
Provides centralized version management for Spring Boot, Spring Framework, and other the libraries used int he project.  
Can be used for other global tasks like static code analysis, documentation generation, etc.

### Future
Content services utilizing UI frameworks such as Angular or React.

## Keycloak
Keycloak running in a Docker container is used for OAuth2 support.
The compose script is locating in the .keycloak folder.

The compose script loads "realm-export.json" configuration file that creates the *smh-club* realm.

Realm Information:
- *smh-club-api* - An OpenID Connect client used by the *club-api* module.
- *smh-club-hateoas* - An OpenID Connect client used by the *club-api-hateoas* module. 
- *smh-club-angular* - An OpenId connect client used by the *club-frontend-angular* module.
- *user@user.com* - A user configured with the *read* role.
- *admin@admin.com* - A user configured with the *read* and *write* roles;
- Both users are configured with the same password of *user*

The master realm admin credentials are *admin/admin*. 

## Next steps
- Add create, update, and delete functionality in the Angular front end app.
- Add e2e testing to the Angular Front end app.
- Prepare Angular front end for *zoneless* operation.
- Create custom themes for the Angular front end app.
- Complete gateway module.
- Create frontend for the *smh-club-hateoas* module.

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
* [Angular Framework](https://angular.dev/)
* [Angular Material](https://material.angular.io/)
* [Angular Testing Framework](https://angular.dev/guide/testing)
* [Angular OAuth2 OIDC Library](https://github.com/manfredsteyer/angular-oauth2-oidc)

## Running the development environment
To run the software 
* Start the Keycloak docker container using *./.keycloak/compose.yaml*.
* Start the *rest-api* module specifying the the *dev* profile.  This will preload the in-memory database.
* Start the *club-frontend-angular* app.  Use the default port of 4200.

## Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
