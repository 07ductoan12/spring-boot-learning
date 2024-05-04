# Spring Boot Annotations

<!--toc:start-->

- [Spring Boot Annotations](#spring-boot-annotations)
  - [Spring Boot Annotations List](#spring-boot-annotations-list)
  - [Core Spring Framework Annotations](#core-spring-framework-annotations)
  - [Spring Framework Stereotype Annotations](#spring-framework-stereotype-annotations)
  - [Spring MVC and REST Annotations](#spring-mvc-and-rest-annotations)
  <!--toc:end-->

## Spring Boot Annotations List

**@SpringBootApplication**: is used to mask the main class of a Spring Boot application. It encapsulates **@SpringBootConfiguration**, **@EnableAutoConfiguration**
and **@ComponentScan** annotations with their defaults attributes.

```java
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

\
**@SpringBootConfiguration**: It is a class-level annotation that is part of the Spring Boot Framework. It implies that a class provides Spring Boot application
configuration. It can be used as an alternative to Spring's standard **@Configuration** annotation so that configuration can be found automatically. Most Spring
Boot Applications use @SpringBootConfiguration via @SpringBootApplication. If an application uses @SpringBootApplication, it is already using @SpringBootConfiguration.

```java
@SpringBootConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public StudentService studentService() {
        return new StudentServiceImpl();
    }
}
```

\
**@EnableAutoConfiguration**: This annotation auto-configures the beans that are present in the classpath. It simplifies the developer's work by assuming the required
beans from the classpath and configure it to run the application. This annotation is part of the spring boot framework. For example, when we illustrate the spring-boot-starter-web
dependency in the classpath, Spring boot auto-configures **Tomcat**, and **Spring MVC**. The package of the class declaring the @EnableAutoConfiguration annotation is considered as the default.
Therefore, we need to apply the @EnableAutoConfiguration annotation in the root package so that every sub-packages and class can be examined.

```java
@Configuration
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

\
**@ConditionalOnClass** Annotation and **@ConditionalOnMissingClass** Annotation: used to mark auto-configuration bean if the class in the annotation’s argument is present/absent.

```java

@Configuration
@ConditionalOnClass(MongoDBService.class)
class MongoDBConfiguration {
    // Insert code here
}
```

\
**@ConditionalOnBean** Annotation and **@ConditionalOnMissingBean** Annotation: These annotations are used to let a bean be included based on the presence or absence of specific beans.

```java
@Bean
@ConditionalOnMissingBean(type = "JpaTransactionManager")
JpaTransactionManager jpaTransactionManager(
    EntityManagerFactory entityManagerFactory)
{
    // Insert code here
}
```

\
**@ConditionalOnProperty** Annotation: These annotations are used to let configuration be included based on the presence and value of a Spring Environment property.

```java
@Bean
@ConditionalOnProperty(name = "usemongodb", havingValue = "local")
DataSource dataSource() {
    // Insert code here
}

@Bean
@ConditionalOnProperty(name = "usemongodb", havingValue = "prod")
DataSource dataSource() {
    // Insert code here
}
```

\
**@ConditionalOnResource** Annotation: These annotations are used to let configuration be included only when a specific resource is present in the classpath.

```java
@ConditionalOnResource(resources = "classpath:mongodb.properties")
Properties additionalProperties() {
    // Insert code here
}
```

\
**@ConditionalOnExpression** Annotation: These annotations are used to let configuration be included based on the result of a SpEL (Spring Expression Language) expression.

```java
@Bean
@ConditionalOnExpression("${env} && ${havingValue == 'local'}")
DataSource dataSource() {
    // Insert code here
}
```

\
**@ConditionalOnCloudPlatform** Annotation: These annotations are used to let configuration be included when the specified cloud platform is active.

```java

@Configuration
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
public class CloudConfigurationExample {
  // Insert code here
}
```

## Core Spring Framework Annotations

**@Required**: It applies to the bean setter method. It indicates that the annotated bean must be populated at configuration time with the required property,
else it throws an exception BeanInitilizationException.

```java
public class Machine {
    private Integer cost;

    @Required
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCost() {
        return cost;
    }
}
```

\
**@Autowired**: Spring provides annotation-based auto-wiring by providing @Autowired annotation. It is used spring bean on setter methods,
instance variable, and constructor. When we use @Autowired annotation, the spring container auto-wires the bean by matching data-type.

```java
@Component
public class Customer{
}

public Person person;

@Autowired
public Customer(Person person){
    this.person = person;
}
```

\
**@Configuration**: It is a class-level annotation. The class annotated width @Configuration used by Spring containers as a source of bean definitions.

```java
@Configuration
public class Vehicle{
    @BeanVehicle engine(){
        return Vehicle();
    }
}
```

\
**@ComponentScan**: tells Spring in which packages you have annotated classes that should be managed by Spring.
So, for example, if you have a class annotated with @Controller which is in a package that is not scanned by Spring, you will not be able to use it as a Spring controller.
So we can say @ComponentScan enables Spring to scan for things like configurations, controllers, services, and other components that are defined.
Generally, @ComponentScan annotation is used with @Configuration annotation to specify the package for Spring to scan for components.

```java
@ComponentScan(basePackages="com.javatpoint")
@Configuration
public class ScanComponent{
    //....
}
```

\
**@Bean**: It is method-level annotation. It is an alternative of XML <bean> tag. It tell the method to produce a bean to be managed by Spring Container.

```java
@Bean
public BeanExample beanExample(){
    return new BeanExample;
}
```

\

## Spring Framework Stereotype Annotations

**@Component**: It is a class-level annotation. It is used to mask a Java class as a bean. A java class annotated with @Component is found during
the classpath. The Spring Framework pick it up and configure it in the application context as a Spring Bean.

```java
@Component
public class Student{

}
```

\
**@Controller**: The @Controller is a class-level annotation. It is a specialization of @Component. It masks a class a web request handler. It is
often used to serve web page. By default, it return a string that indicates which route to redirect. It is mostly used with @RequestMapping annotation.

```java
@Controller
@RequestMapping("books")
public class BooksController{
    @RequestMapping(value = "/{name}", method = RequestMapping.Get)
    public Employee getBooksByName(){
        return booksTemplate;
    }
}
```

\
**@Service**: It is also used at class level. It tells the Spring that class contains the business logic.

```java
package com.javatpoint

@Service
public class TestService{
    public void service1(){
        // business code
    }
}
```

\
**@Repository**: It is a class-level annotation. The repository is a DAOs (Data Access Object) that access the database directly. The repository does all
the operations related to the database.

```java
package com.javatpoint

@Repository
public class TestRepository{
    public void delete(){
        //persistence code
    }
}
```

## Spring MVC and REST Annotations

**@RequestMapping**: It is used to map the web requests. It has many optional elements like **consumes, header, method, name, params, path, produces,** and **value**.
We use it with the class as well as the method.

```java
@Controller
public class BooksController{
    @RequestMapping("/computer-science/books")
    public String getAllBooks(Model model){
        return "bookList";
    }
}
```

\
**@GetMapping**: It maps the **HTTP GET** requests on the specific handler method. It is used to create a web service endpoint that **fetches**
It is used instead of using: **@RequestMapping(method = RequestMethod.GET)**.\
**@PostMapping**: It maps the **HTTP POST** requests on the specific handler method. It is used to create a web service endpoint that **creates** It is
used instead of using: **@RequestMapping(method = RequestMethod.POST)**.\
**@PutMapping**: It maps the **HTTP PUT** requests on the specific handler method. It is used to create a web service endpoint that creates or updates
It is used instead of using: **@RequestMapping(method = RequestMethod.PUT)**.\
**@DeleteMapping**: It maps the **HTTP DELETE** requests on the specific handler method. It is used to create a web service endpoint that deletes a resource.
It is used instead of using: **@RequestMapping(method = RequestMethod.DELETE)**.\
**@PatchMapping**: It maps the **HTTP PATCH** requests on the specific handler method. It is used instead of using: **@RequestMapping(method = RequestMethod.PATCH)**.\
**@RequestBody**: It is used to **bind** HTTP request with an object in a method parameter. Internally it uses **HTTP MessageConverters** to convert the body of the request.
When we annotate a method parameter with **@RequestBody**, the Spring framework binds the incoming HTTP request body to that parameter.\
**@ResponseBody**: It binds the method return value to the response body. It tells the Spring Boot Framework to serialize a return an object into JSON and XML format.\
**@PathVariable**: It is used to extract the values from the URI. It is most suitable for the RESTful web service, where the URL contains a path variable. We can define multiple @PathVariable in a method.\
**@RequestParam**: It is used to extract the query parameters form the URL. It is also known as a **query parameter**.
It is most suitable for web applications. It can specify default values if the query parameter is not present in the URL.\
**@RequestHeader**: It is used to get the details about the HTTP request headers. We use this annotation as a **method parameter**.
The optional elements of the annotation are **name, required, value, defaultValue**. For each detail in the header, we should specify separate annotations. We can use it multiple time in a method\
**@RestController**: It can be considered as a combination of **@Controller** and **@ResponseBody** annotations.
The @RestController annotation is itself annotated with the @ResponseBody annotation. It eliminates the need for annotating each method with @ResponseBody.\
**@RequestAttribute**: It binds a method parameter to request attribute. It provides convenient access to the request attributes from a controller method.
With the help of @RequestAttribute annotation, we can access objects that are populated on the server-side.\
