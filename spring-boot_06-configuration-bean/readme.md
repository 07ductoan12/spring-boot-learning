# Content

<!--toc:start-->

- [Content](#content)
  - [@Configuration và @Bean](#configuration-và-bean)
  - [In Background](#in-background)
  - [Ý nghĩa sử dụng @Configuration và @Bean](#ý-nghĩa-sử-dụng-configuration-và-bean)
  - [Ví dụ:](#ví-dụ)
  - [Bean có tham số](#bean-có-tham-số)
  <!--toc:end-->

## @Configuration và @Bean

`@Configuration` là một Annotation đánh dấu trên một Class cho phép **Spring Boot** biết được đó là Bean.
`@Bean` là một Annotation được đánh dấu trên các `method` cho phép **Spring Boot** biết được đây là _Bean_ và thực hiện inject _Bean_ vào `Context`. `@Bean` nằm trong các class có đánh dấu `@Configuration`.

Ví dụ:
_SimpleBean.java_

```java
/** SimpleBean */
public class SimpleBean {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SimpleBean(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "This is a simple bean, name: " + username;
    }
}
```

_AppConfig.java_

```java
@Configuration
public class AppConfig {

    @Bean
    SimpleBean simpleBeanConfigure() {
        return new SimpleBean("toan");
    }
}
```

Output:

```text
Simple Bean Example: This is a simple bean, name: toan
```

`SimpleBean` là một object được quản lý trong `Context` của **Spring Boot**, mặc dù không sử dụng tới các khái niệm `@Component`.

## In Background

Sau khi **Spring Boot** lần đầu khởi chạy, ngoài việc tìm các `@Component` thì còn tìm các class `@Configuration`.

1. Đi tìm class có đánh dấu `@Component`.
2. Tạo ra đối tượng từ class có đánh dấu `@Configuration`.
3. Tìm các method có đánh dấu `@Bean` trong đối tượng vừa tạo.
4. Thực hiện gọi các method có đánh dấu `@Bean` để lấy ra các _Bean_ đưa vào `Context`.

Về bản chất, `@Configuration` cũng là `@Component`. Nó chỉ khác ở ý nghĩa sử dụng.

## Ý nghĩa sử dụng @Configuration và @Bean

Trong thực tế, khi một `@Bean` có quá nhiều logic để khởi tạo và cấu hình thì nên sử dụng `@Configuration` và `@Bean` để tự tay tạo ra `Bean` (có thể hiểu phần nào là chúng ta đang _config_ cho chương trình).

## Ví dụ:

_DatabaseConnector.java_

```java
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

_MongoDbConnector.java_

```java
public class MongoDbConnector extends DatabaseConnector {

    @Override
    public void connect() {
        // TODO Auto-generated method stub
        System.out.println("Đã kết nối tới Mongodb: " + getUrl());
    }
}
```

_MySqlConnector.java_

```java
/** MysqlConnector */
public class MysqlConnector extends DatabaseConnector {

    @Override
    public void connect() {
        System.out.println("Đã kết nối tới Mysql: " + getUrl());
    }
}
```

_PostgreSqlConnector.java_

```java
/** PostgreSqlConnector */
public class PostgreSqlConnector extends DatabaseConnector {

    @Override
    public void connect() {
        System.out.println("Đã kết nối tới Postgresql: " + getUrl());
    }
}
```

_AppConfig.java_

```java
/** AppConfig */
@Configuration
public class AppConfig {

    @Bean
    SimpleBean simpleBeanConfigure() {
        return new SimpleBean("toan");
    }

    @Bean("mysqlConnector")
    DatabaseConnector mysqlConnector() {
        DatabaseConnector mysqlConnector = new MysqlConnector();
        mysqlConnector.setUrl("jdbc:mysql://host1:33060/toan");
        return mysqlConnector;
    }

    @Bean("mongodbConnector")
    DatabaseConnector mongodbConfigure() {
        DatabaseConnector mongoDbConnector = new MongoDbConnector();
        mongoDbConnector.setUrl("mongodb://mongodb0.example.com:27017/toan");
        // Set username, password, format, v.v...
        return mongoDbConnector;
    }

    @Bean("postgresqlConnector")
    DatabaseConnector postgresqlConfigure() {
        DatabaseConnector postgreSqlConnector = new PostgreSqlConnector();
        postgreSqlConnector.setUrl("postgresql://localhost/toan");
        // Set username, password, format, v.v...
        return postgreSqlConnector;
    }
}
```

_App.java_

```java
/** App */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        SimpleBean simpleBean = context.getBean(SimpleBean.class);

        System.out.println("Simple Bean Example: " + simpleBean.toString());

        DatabaseConnector mysql = (DatabaseConnector) context.getBean("mysqlConnector");
        mysql.connect();

        DatabaseConnector mongodb = (DatabaseConnector) context.getBean("mongodbConnector");
        mongodb.connect();

        DatabaseConnector postgresql = (DatabaseConnector) context.getBean("postgresqlConnector");
        postgresql.connect();
    }
}
```

Output:

```text
Simple Bean Example: This is a simple bean, name: toan
Đã kết nối tới Mysql: jdbc:mysql://host1:33060/toan
Đã kết nối tới Mongodb: mongodb://mongodb0.example.com:27017/toan
Đã kết nối tới Postgresql: postgresql://localhost/toan
```

## Bean có tham số

Ví dụ:
_AppConfig.java_

```java
@Configuration
public class AppConfig {

    @Bean
    SimpleBean simpleBeanConfigure() {
        return new SimpleBean("toan");
    }

    @Bean("mysqlConnector")
    DatabaseConnector mysqlConnector(SimpleBean simpleBean) {
        DatabaseConnector mysqlConnector = new MysqlConnector();
        mysqlConnector.setUrl("jdbc:mysql://host1:33060/" + simpleBean.getUsername());
        return mysqlConnector;
    }
}
```

Output:

```text
Simple Bean Example: This is a simple bean, name: toan
Đã kết nối tới Mysql: jdbc:mysql://host1:33060/toan
Đã kết nối tới Mongodb: mongodb://mongodb0.example.com:27017/toan
Đã kết nối tới Postgresql: postgresql://localhost/toan
```

-> Nếu method được đánh dấu bằng `@Bean` có tham số truyền vào, thì **Spring Boot** sẽ tự inject các Bean đã có trong `Context` vào tham số.
