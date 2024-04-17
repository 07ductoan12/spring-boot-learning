# content

<!--toc:start-->

- [content](#content)
  - [Ví dụ](#ví-dụ)
  - [@ComponentScan](#componentscan)
  - [Multiple package scan](#multiple-package-scan)
  <!--toc:end-->

## Ví dụ

Tạo ra 2 Bean:

1. `Girl`: nằm cùng package với `App`.
2. `otherGirl`: nằm trong package con `others`. `others` cùng cấp với `App`.

_Girl.java_

```java
@Component
public class Girl {

    @Override
    public String toString() {
        return "Girl.java";
    }
}
```

_OtherGirl.java_

```java
@Component
public class OtherGirl {

    @Override
    public String toString() {
        return "otherGirl.java";
    }
}
```

_App.java_

```java
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        try {
            Girl girl = context.getBean(Girl.class);
            System.out.println("Bean: " + girl.toString());
        } catch (Exception e) {
            System.out.println("Bean Girl không tồn tại");
        }

        try {
            OtherGirl otherGirl = context.getBean(OtherGirl.class);
            if (otherGirl != null) {
                System.out.println("Bean: " + otherGirl.toString());
            }
        } catch (Exception e) {
            System.out.println("Bean Girl không tồn tại");
        }
    }
}
```

Output:

```text
Bean: Girl.java
Bean: otherGirl.java
```

Kết quả in ra màn hình cả 2 bean `Girl` và `OtherGirl` đều đc tạo ra trong `context` -> **Spring Boot** tìm được các `Bean` trên các file bên cạnh `App` và những package con bên cạnh `App`.

## @ComponentScan

Trong trường hợp muốn tùy chỉnh cấu hình cho **Spring Boot** chỉ tìm kiếm các bean trong một package nhất định:

1. Sử dụng `@ComponentScan`.
2. Sử dụng `scanBasePackages` trong `@SpringBootApplication`.

Cách 1: `@ComponentScan`:

```java
/** App */
@ComponentScan("com.example.others")
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        try {
            Girl girl = context.getBean(Girl.class);
            System.out.println("Bean: " + girl.toString());
        } catch (Exception e) {
            System.out.println("Bean Girl không tồn tại");
        }

        try {
            OtherGirl otherGirl = context.getBean(OtherGirl.class);
            if (otherGirl != null) {
                System.out.println("Bean: " + otherGirl.toString());
            }
        } catch (Exception e) {
            System.out.println("Bean Girl không tồn tại");
        }
    }
}
```

Output:

```text
Bean Girl không tồn tại
Bean: otherGirl.java
```

cách 2: `scanBasePackages`

```java
@SpringBootApplication(scanBasePackages = "com.example.others")
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        try {
            Girl girl = context.getBean(Girl.class);
            System.out.println("Bean: " + girl.toString());
        } catch (Exception e) {
            System.out.println("Bean Girl không tồn tại");
        }

        try {
            OtherGirl otherGirl = context.getBean(OtherGirl.class);
            if (otherGirl != null) {
                System.out.println("Bean: " + otherGirl.toString());
            }
        } catch (Exception e) {
            System.out.println("Bean Girl không tồn tại");
        }
    }
}
```

Output:

```text
Bean Girl không tồn tại
Bean: otherGirl.java
```

## Multiple package scan

```java
@ComponentScan({"com.example.others1", "com.example.others2"})
```

hoặc

```java
@SpringBootApplication(scanBasePackages = {"com.example.others1", "com.example.others2"})
```
