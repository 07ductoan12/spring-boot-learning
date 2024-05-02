# Content

<!--toc:start-->

- [Content](#content)
  - [Spring Profiles](#spring-profiles)
  - [Tạo file config](#tạo-file-config)
  - [Kích hoạt config](#kích-hoạt-config)
  - [3. Cách sử dụng @Profile](#3-cách-sử-dụng-profile)
  - [Demo](#demo) - [Tạo model](#tạo-model) - [Chạy thử](#chạy-thử)
  <!--toc:end-->

## Spring Profiles

`Spring Profiles` là một core feature trong **Spring Boot**, cho phép cấu hình ứng dụng, active/deactive `Bean` tùy theo môi trường.

## Tạo file config

`Spring Profiles` có sẵn trong Framework nên không cần cài thêm thư viện nào.
Để sử dụng, các file config tại thư mục `resource` trong project. Mặc định Spring sẽ nhận các file có tên như sau:

```java
application.properties
application.yml
application-{profile-name}.yml // .properties
```

ví dụ có 2 môi trường là `local` và `aws`:

```java
application.yml
application-local.yml
application-aws.yml
application-common.yml
```

`application` là file config chính khai báo các enviroment.
`application-aws` chỉ sử dụng khi chạy chương trình ở local.
`application-aws` chỉ sử dụng khi chạy ở AWS.
`application-common` là những config dùng chung (môi trường nào cũng cần).

Khai báo từng file:
_application.yml_

```yml
#application.yml
---
spring.profiles: local
spring.profiles.include: common, local
---
spring.profiles: aws
spring.profiles.include: common, aws
---
```

_application-aws.yml_

```yml
spring:
  datasource:
    username: xxx
    password: xxx
    url: jdbc:mysql://10.127.24.12:2030/news?useSSL=false&characterEncoding=UTF-8
```

_application-local.yml_

```yml
spring:
  datasource:
    username: root
    password:
    url: jdbc:mysql://localhost:3306/news?useSSL=false&characterEncoding=UTF-8

logging:
  level:
    org:
      hibernate:
        SQL: debug
```

_application-common.yml_

```yml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 50
          batch_versioned_data: true
    hibernate:
      ddl-auto: none
```

Trong file _application.yml_ có hai khai báo môi trường là `local` và `aws`. Tại môi trường sẽ `include` các file config, ví dụ khi khích hoạt `aws`, **Spring** sẽ load tất cả các config có trong `application-common.yml` và `application-aws.yml`.

## Kích hoạt config

Để sử dụng một `Profiles` có các cách:
**#1: Sử dụng `spring.profiles.active` trong file `application.properties` hoặc `application.yml`**

```java
spring.profiles.active=aws
```

**#2: Active trong code, trước khi chạy trương trình**

```java
@Configuration
public class ApplicationInitializer
  implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter(
          "spring.profiles.active", "aws");
    }
}
```

hoặc

```java
@Autowired
private ConfigurableEnvironment env;
...
env.setActiveProfiles("aws");
```

hoặc

```java
SpringApplication application = new SpringApplication(SpringBootProfilesApplication.class);
ConfigurableEnvironment environment = new StandardEnvironment();
environment.setActiveProfiles("aws");
application.setEnvironment(environment);
application.run(args);
```

**#3: Sử dụng JVM System Parameter (nên dùng)**

```java
-Dspring.profiles.active=aws
```

**#4: Environment Variable (Unix) (nên dùng)**

```java
export SPRING_PROFILES_ACTIVE=aws
```

## 3. Cách sử dụng @Profile

Khi đã có `Profile`, ngoài các biến toàn cục được thay đổi theo môi trường, cũng có thể quyết định xem trong code rằng `Bean` hay `Class` nào sẽ được quyền chạy ở môi trường nào. Bằng cách sử dụng annotation `@Profile`.

```java
// Bean này Spring chỉ khởi tạo và quản lý khi môi trường là `local`
@Component
@Profile("local")
public class LocalDatasourceConfig
```

ví dụ:

```java
// Bean này Spring chỉ khởi tạo và quản lý khi môi trường là những môi trường không phải là `local`
@Component
@Profile("!local")
public class LocalDatasourceConfig
```

## Demo

### Tạo model

_LocalDatasource.java_

```java

```

### Chạy thử
