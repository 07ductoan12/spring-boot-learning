<!--toc:start-->

- [Cách chạy ứng dụng Spring Boot](#cách-chạy-ứng-dụng-spring-boot)
- [@Component](#component)
- [@Autowired](#autowired)
- [Singleton](#singleton)
<!--toc:end-->

#Detail

## Cách chạy ứng dụng Spring Boot

Trong **Spring Boot**, ta sẽ phải chỉ cho **Spring Boot** biết nơi nó khởi chạy lần đầu, để nó cài đặt mọi thứ.

Cách thực hiện là thêm annotation `@SpringBootApplication` trên class chính và gọi `SpringApplication.run(App.class, args);` để chạy project.

```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

Một trong những nhiệm vụ chính của **Spring** là tạo ra một cái **_Container_** chứa các **_Dependency_**.

`SpringApplication.run(App.class, args)` chính là câu lệnh để tạo ra **_container_**. Sau đó nó tìm toàn bộ các **_dependency_** trong project và đưa vào đó.

Spring đặt tên cho **_container_** là `ApplicationContext`

và đặt tên cho các **_dependency_** là `Bean`

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // ApplicationContext chứa toàn bộ dependency trong project.
        ApplicationContext context = SpringApplication.run(App.class, args);
    }
}
```

## @Component

`@Component` là một `Annotation` (chú thích) đánh dấu trên các `Class` để giúp **Spring** biết nó là một `Bean`.

Ví dụ:

interface `Outfit`

```java
public interface Outfit {
    public void wear();
}
```

implement nó là Class `Jean`

```java
/*
 Đánh dấu class bằng @Component
 Class này sẽ được Spring Boot hiểu là một Bean (hoặc dependency)
 Và sẽ được Spring Boot quản lý
*/
@Component
public class Jean implements Outfit {
    @Override
    public void wear() {
        System.out.println("Mặc jean");
    }
}
```

Chạy chương trình, và xem kết quả:

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // ApplicationContext chính là container, chứa toàn bộ các Bean
        ApplicationContext context = SpringApplication.run(App.class, args);

        // Khi chạy xong, lúc này context sẽ chứa các Bean có đánh
        // dấu @Component.

        // Lấy Bean ra bằng cách
        Outfit outfit = context.getBean(Outfit.class);

        // In ra để xem thử nó là gì
        System.out.println("Instance: " + outfit);
        // xài hàm wear()
        outfit.wear();
    }
}
```

Output:

```
[1] Instance: me.loda.spring.helloworld.Jean@1e1f6d9d
[2] Mặc bikini
```

`Outfit` lúc này chính là `Jean`. Class đã được đánh dấu là `@Component`.

**Spring Boot** khi chạy sẽ dò tìm toàn bộ các _Class cùng cấp_ hoặc ở trong các _package thấp hơn_ (Chúng ta có thể cấu hình việc tìm kiếm này, sẽ đề cập sau). Trong quá trình dò tìm này, khi gặp một class được đánh dấu `@Component` thì nó sẽ tạo ra một instance và đưa vào `ApplicationContext` để quản lý.

## @Autowired

Tạo một Class `Girl` và có một thuộc tính là `Outfit`.

Đánh dấu `Girl` là một `@Component`. Tức **Spring Boot** cần tạo ra một instance của `Girl` để quản lý.

```java
@Component
public class Girl {

    @Autowired
    Outfit outfit;

    public Girl(Outfit outfit) {
        this.outfit = outfit;
    }

    // GET
    // SET
}

```

Đánh dấu thuộc tính `Outfit` của `Girl` bởi Annotation `@Autowired`. Điều này nói với **Spring Boot** hãy tự _inject_ (tiêm) một instance của `Outfit` vào thuộc tính này khi khởi tạo `Girl`.

Chạy thử chương trình.

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // ApplicationContext chính là container, chứa toàn bộ các Bean
        ApplicationContext context = SpringApplication.run(App.class, args);

        // Khi chạy xong, lúc này context sẽ chứa các Bean có đánh
        // dấu @Component.

        // Lấy Bean ra bằng cách
        Outfit outfit = context.getBean(Outfit.class);

        // In ra để xem thử nó là gì
        System.out.println("Output Instance: " + outfit);
        // xài hàm wear()
        outfit.wear();

        Girl girl = context.getBean(Girl.class);

        System.out.println("Girl Instance: " + girl);

        System.out.println("Girl Outfit: " + girl.outfit);

        girl.outfit.wear();
    }
}
```

Output:

```text
[1] Output Instance: me.loda.spring.helloworld.Jean@2e16f13a
[2] Mặc bikini
[3] Girl Instance: me.loda.spring.helloworld.Girl@353cb1cb
[4] Girl Outfit: me.loda.spring.helloworld.Jean@2e16f13a
[5] Mặc bikini
```

**Spring Boot** đã tự tạo ra một `Girl` và trong quá trình tạo ra đó, nó truyền `Outfit` vào làm thuộc tính.

## Singleton

Điều đặc biệt là các `Bean` được quản lý bên trong `ApplicationContext` đều là singleton.

```
[1] Output Instance: me.loda.spring.helloworld.Jean@2e16f13a

[4] Girl Outfit: me.loda.spring.helloworld.Jean@2e16f13a
```

`Outfit` ở 2 đối tượng trên là một.

Tất cả những `Bean` được quản lý trong `ApplicationContext` đều chỉ được tạo ra **một lần duy nhất** và khi có `Class` yêu cầu `@Autowired` thì nó sẽ lấy đối tượng có sẵn trong `ApplicationContext` để _inject_ vào.

Trong trường hợp muốn mỗi lần sử dụng là một instance hoàn toàn mới. Thì hãy đánh dấu `@Component` đó bằng `@Scope("prototype")`

```java
@Component
@Scope("prototype")
public class Jean implements Outfit {
    @Override
    public void wear() {
        System.out.println("Mặc Jean");
    }
}
```
