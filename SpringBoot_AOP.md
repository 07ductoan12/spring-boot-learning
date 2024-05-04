# Spring Boot AOP (Aspect-Oriented Programming)

<!--toc:start-->

- [Spring Boot AOP (Aspect-Oriented Programming)](#spring-boot-aop-aspect-oriented-programming)
  - [AOP](#aop)
  - [Benefits of AOP](#benefits-of-aop)
  - [AOP Terminology](#aop-terminology)
  <!--toc:end-->

The application is generally developed with multiple layers. A typical Java application has the following layers:

- **Web Layer**: It exposes the **services** using the REST or web application.
- **Business Layer**: It implements the **business logic** of an application.
- **Data Layer**: It implements the **persistence logic** of the application.

The responsibility of each layer is different, but there are a few common aspects that apply that apply to all layers are **Logging, Security, validation, caching,** etc.
These common aspects are called **cross-cutting concerns**.

If we implement these concerns in each layer separately, the code becomes more difficult to maintain. To overcome this problem, **Aspect-Oriented Programming** (AOP) provides
a solution to implement cross-cutting concerns.

- Implement the cross-cutting concerns as an aspect.
- Define pointcuts to indicate where the aspect has to be applied.

## AOP

AOP **(Aspect-Oriented Programming)** is a programming pattern that increases modularity by allowing the separation of the **cross-cutting concern**. The cross-cutting concerns are
different from the main business logic. We can add additional behavior to existing code without modification of the code itself.

Using AOP, we define common functionality in one place. We are free to define how and where this functionality is applied without modifying the class to which we are applying
the new feature. The cross-cutting concern can now be modularized into special class, call **aspect**.

**Two** benefits of aspects:

- First, the logic for each concern is now in one place instead of scattered all over the codebase.
- Second, the business modules only contain code for their primary concern. The secondary concern has been moved to the **aspect**.

## Benefits of AOP

- It is implemented in pure Java.
- There is no requirement for a special compilation process.
- It supports only method execution Join points.
- Only run time weaving is available.
- Two types of AOP proxy is available: **JDK dynamic proxy** and **CGLIB proxy**.

## AOP Terminology

- **Aspect**: An aspect is a module that encapsulates **advice** and **pointcuts** and provides cross-cutting An application can have any number of aspects.
  We can implement an aspect using regular class annotated with **@Aspect** annotation.
- **Pointcut**: A pointcut is an expression that selects one or more join points where advice is executed. We can define pointcuts using **expressions** or **patterns**.
  It uses different kinds of expressions that matched with the join points. In Spring Framework, **AspectJ** pointcut expression language is used.
- **Join point**: A join point is a point in the application where we apply an **AOP aspect**.
  Or it is a specific execution instance of an advice. In AOP, join point can be a **method execution, exception handling, changing object variable value,** etc.
- **Advice**: The advice is an action that we take either **before** or **after** the method execution.
  The action is a piece of code that invokes during the program execution. There are **five types** of advices in the Spring AOP framework: **before, after, after-returning, after-throwing,** and around advice.
  Advices are taken for a particular **join point**. We will discuss these advices further in this section.
- **Target object**: An object on which advices are applied, is called the **target object**.
  Target objects are always a **proxied** It means a subclass is created at run time in which the target method is overridden, and advices are included based on their configuration.
- **Weaving**: It is a process of **linking aspects** with other application types. We can perform weaving at **run time, load time**, and **compile time**.
