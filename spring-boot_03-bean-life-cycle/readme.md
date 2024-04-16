# Content

<!--toc:start-->

- [Content](#content)
  - [@PostConstruct](#postconstruct)
  - [@PreDestroy](#predestroy)
  - [Bean Life Cycle](#bean-life-cycle)
  <!--toc:end-->

## @PostConstruct

`@PostConstruct` được đánh dấu trên một method duy nhất bên trong `Bean`. `IoC Container` hoặc `ApplicationContext` sẽ gọi hàm này sau khi một `Bean` được tạo và quản lý.

```java
@Component
public class Girl {

    @PostConstruct
    public void postConstruct() {
        System.out.println("\t>> Đối tượng Girl sau khi khởi tạo xong sẽ chạy hàm này");
    }
}
```

## @PreDestroy

`@PreDestroy` được dánh dấu trên một method duy nhất bên trong `Bean`. `IoC Container` hoặc `ApplicationContext` sẽ gọi hàm này trước khi một `Bean` bị xóa hoặc không quản lý nữa.

```java
@Component
public class Girl{

    @PreDestroy
    public void PreDestroy(){
        System.out.println("\t>> Đối tượng Girl trước khi bị destroy thì chạy hàm này");
    }
}
```

## Bean Life Cycle

**Spring Boot** từ thời điểm chạy lần đầu tới khi bị kết thúc thì các `Bean` nó quản lý sẽ có một vòng đời:

1. Khi `IoC Container` (`ApplicationContext`) tìm thấy một Bean cần quản lý, nó sẽ khởi tạo bằng `Constructor`.
2. Inject dependencies vào `Bean` bằng Setter, và thực hiện các quá trình cài đặt khác vào `Bean` như `setBeanName`, `setBeanClassLoader`, ...
3. Hàm đánh dấu `@PostConstruct` được gọi.
4. Tiền xử lý sau khi `@PostContruct` được gọi.
5. `Bean` sẵn sàng để hoạt động.
6. Nếu `Ioc Container` không quản lý bean nữa hoặc bị shutdown hàm `@PreDestroy` trong `Bean`.
7. Xóa `Bean`.

Ví dụ:

```java
@Component
public class Girl {

    @PostConstruct
    public void postConstruct(){
        System.out.println("\t>> Đối tượng Girl sau khi khởi tạo xong sẽ chạy hàm này");
    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("\t>> Đối tượng Girl trước khi bị destroy thì chạy hàm này");
    }
}
```

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // ApplicationContext chính là container, chứa toàn bộ các Bean
        System.out.println("> Trước khi IoC Container được khởi tạo");
        ApplicationContext context = SpringApplication.run(App.class, args);
        System.out.println("> Sau khi IoC Container được khởi tạo");

        // Khi chạy xong, lúc này context sẽ chứa các Bean có đánh
        // dấu @Component.

        Girl girl = context.getBean(Girl.class);

        System.out.println("> Trước khi IoC Container destroy Girl");
        ((ConfigurableApplicationContext) context).getBeanFactory().destroyBean(girl);
        System.out.println("> Sau khi IoC Container destroy Girl");

    }
}
```

Output:

```text
> Trước khi IoC Container được khởi tạo

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.4)

2024-04-16T21:24:45.753+07:00  INFO 292837 --- [           main] com.example.App                          : Starting App v0.0.1-SNAPSHOT using Java 21.0.2 with PID 292837 (/home/toan/d/learn/java/springboot/spring-boot-learning/spring-boot_03-bean-life-cycle/target/spring-boot_03-bean-life-cycle-0.0.1-SNAPSHOT.jar started by toan in /home/toan/d/learn/java/springboot/spring-boot-learning)
2024-04-16T21:24:45.755+07:00  INFO 292837 --- [           main] com.example.App                          : No active profile set, falling back to 1 default profile: "default"
2024-04-16T21:24:46.203+07:00  INFO 292837 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2024-04-16T21:24:46.209+07:00  INFO 292837 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-04-16T21:24:46.209+07:00  INFO 292837 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.19]
2024-04-16T21:24:46.224+07:00  INFO 292837 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-04-16T21:24:46.225+07:00  INFO 292837 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 434 ms
        >> Đối tượng Girl sau khi khởi tạo xong sẽ chạy hàm này
2024-04-16T21:24:46.388+07:00  INFO 292837 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2024-04-16T21:24:46.395+07:00  INFO 292837 --- [           main] com.example.App                          : Started App in 0.856 seconds (process running for 1.081)
> Sau khi IoC Container được khởi tạo
> Trước khi IoC Container destroy Girl
        >> Đối tượng Girl trước khi bị destroy thì chạy hàm này
> Sau khi IoC Container destroy Girl
```
