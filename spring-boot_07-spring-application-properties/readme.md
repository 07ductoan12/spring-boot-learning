# Content

<!--toc:start-->

- [Content](#content)
  - [application.properties](#applicationproperties)
  - [@Value](#value)
  - [Ví dụ](#ví-dụ)
  <!--toc:end-->

## application.properties

Trong **Spring Boot**, các thông tin cấu hình mặc định được lấy từ file `resources/applications.properties`.

Ví dụ:
Khi muốn **Spring Boot** chạy trên port 8081 thay cho 8080:
_application.properties_

```text
server.port = 8081
```

hoặc chuyển sang dạng DEBUG:

```text
logging.level.root=DEBUG
```

## @Value

Trong trường hợp các giá trị config của cá nhân, **Spring Boot** cung cấp annotation `@Value`.

_application.properties_

```
toan.mysql.url=jdbc:mysql://host1:33060/toan
```

`@Value` dựa trên thuộc tính của class, Có nhiệm vụ lấy thông tin từ properties và gán vào biến.

```java
/** AppConfig */
public class AppConfig {

    @Value("${toan.mysql.url}")
    String mysql;
}
```

Thông tin truyền vào annotation `@Value`: tên của cấu hình trong dấu `${name}`.

## Ví dụ

_application.properties_

```text
server.port = 8081
logging.level.root=INFO

toan.mysql.url=jdbc:mysql://host1:33060/toan
```

_DatabaseConnector.java_

```java
/** DatabaseConnector */
public abstract class DatabaseConnector {
    private String url;

    public abstract void connect();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
```

_MySqlConnector.java_

```java
/** MySqlConnector */
public class MySqlConnector extends DatabaseConnector {

    @Override
    public void connect() {
        System.out.println("Đã kết nối tới Mysql: " + getUrl());
    }
}
```

cấu hình chương trình `AppConfig`
_AppConfig.java_

```java
/** AppConfig */
@Configuration
public class AppConfig {

    @Value("${toan.mysql.url}")
    String mysqlUrl;

    @Bean
    DatabaseConnector mysqlConfigure() {
        DatabaseConnector mysqlConnector = new MySqlConnector();

        System.out.println("Config Mysql Url: " + mysqlUrl);
        mysqlConnector.setUrl(mysqlUrl);

        return mysqlConnector;
    }
}
```

Chạy thử chương trình:

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        DatabaseConnector databaseConnector = context.getBean(DatabaseConnector.class);
        databaseConnector.connect();
    }
}
```

Output:

```text
2024-04-17T11:48:42.193+07:00  INFO 444126 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8081 (http)
2024-04-17T11:48:42.199+07:00  INFO 444126 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-04-17T11:48:42.199+07:00  INFO 444126 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.19]
2024-04-17T11:48:42.214+07:00  INFO 444126 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-04-17T11:48:42.214+07:00  INFO 444126 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 434 ms
Config Mysql Url: jdbc:mysql://host1:33060/toan
2024-04-17T11:48:42.376+07:00  INFO 444126 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8081 (http) with context path ''
2024-04-17T11:48:42.383+07:00  INFO 444126 --- [           main] com.example.App                          : Started App in 0.883 seconds (process running for 1.11)
Đã kết nối tới Mysql: jdbc:mysql://host1:33060/toan
```
