# Spring Boot Layers

<!--toc:start-->

- [Spring Boot Layers](#spring-boot-layers)
- [Spring Boot Flow Architecture](#spring-boot-flow-architecture)
<!--toc:end-->

The spring boot consists of the following four layers:

- Presentation Layer - Authentication & Json Translation
- Business Layer - Business Logic, Validation & Authorization
- Persistence Layer - Storage Logic
- Database Layser - Actual Database

**1. Presentation Layer**
\
The presentation layer is the top layer of spring boot architecture. It consists of Views. i.e, the front-end part of the application. It handlers the HTTP requests
and performs authentication. It is responsible for converting the JSON field's parameter to Java Object and vice-versa. Once it performs the authentication of the request it passes
it to the next layer. i.e., the business layer.
\
**2. Business Layer**
\
The business layer contains all the business logic. It consists of services classes. It is responsible of validation and authorization.
\
**3. Persistence Layer**
\
The persistence layer contains all the database storage logic. It is responsible for converting business object to the database row and vice-versa.
\
**4. Database Layer**
\
The database layer contains all the databases such as MySql, MongoDB, etc. This layer can contain multiple databases. It is responsible for performing the CRUD operations.

# Spring Boot Flow Architecture

![Spring Boot Flow Architecture](./assets/springboot_flow_architecture.jpg) \* Spring boot flow architecture.
\
**Explanation:**

- The Client makes an **HTTP** request(GET, PUT, POST, etc.)
- The HTTP request is forwarded to the **Controller**. The controller maps the request. It processes the handles and calls the server logic.
- The business logic is performed in the **Service layer**. The spring boot performs all the logic over the data of the database which is mapped to the spring boot model class through Java Persistence Library **(JPA)**.
- The JSP page is returned as Response from the controller.
