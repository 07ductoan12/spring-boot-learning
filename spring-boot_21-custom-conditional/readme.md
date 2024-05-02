# Content

<!--toc:start-->

- [Content](#content)
  - [Tự tạo @Conditional](#tự-tạo-conditional)
  - [Tự tạo Annotation @Conditional](#tự-tạo-annotation-conditional)
  - [Chạy thử](#chạy-thử)
  - [Kết hợp nhiều điều kiện với OR](#kết-hợp-nhiều-điều-kiện-với-or)
  - [Kết hợp với điều kiện AND](#kết-hợp-với-điều-kiện-and)
  <!--toc:end-->

## Tự tạo @Conditional

Để tạo ra một điều kiện, cần kế thừa lớp `Condition`, và implement lại function `matches`. `matches` là nơi kiểm tra điều kiện xem có thỏa mãn không.

```java

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/** WindowRequired Một điều kiện, phải kế thừa lớp Condition của Spring Boot cung cấp */
public class WindowRequired implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Nếu OS ra window trả ra true.
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
```

Có thể sử dụng như sau:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/** AppConfiguration */
@Configuration
public class AppConfiguration {
    public static class SomeBean {}

    @Conditional(WindowRequired.class)
    @Bean
    SomeBean someBean() {
        return new SomeBean();
    }
}
```

## Tự tạo Annotation @Conditional

Ngoài việc viết `@Conditional(WindowRequired.class)`, có thể tự tạo một `Annotation` giống với **Spring Boot** (giống như `@ConditionalOnClass`).

```java
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** ConditionalOnWindow */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// Đánh dấu annotation này bởi @Conditional(WindowRequired.class)
@Conditional(WindowRequired.class)
public @interface ConditionalOnWindow {
    /*
    Trong trường hợp bạn muốn viết ngắn gọn,
    hay tạo ra 1 Annotation mới và gắn @Conditional(WindowRequired.class)
    trên nó

    Như vậy khi cần sử dụng chỉ cần gọi @ConditionalOnWindow là được
     */

}
```

Khi sử dụng

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** AppConfiguration */
@Configuration
public class AppConfiguration {
    public static class SomeBean {}

    // @Conditional(WindowRequired.class)
    @ConditionalOnWindow
    @Bean
    SomeBean someBean() {
        return new SomeBean();
    }
}
```

## Chạy thử

```java
import com.example.AppConfiguration.SomeBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/** App */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        try {
            SomeBean someBean = context.getBean(SomeBean.class);
            System.out.println("SomeBean tồn tại!");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("SomeBean chỉ được tạo nếu chạy trên Window");
        }
    }
}
```

## Kết hợp nhiều điều kiện với OR

Có thể kết hợp nhiều điều kiện với nhau bởi phép OR. Spring Boot hỗ trợ điều này bằng kế thừa lớp `AnyNestedCondition`.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/** AppConfiguration */
@Configuration
public class AppConfiguration {
    public static class SomeBean {}

    // @Conditional(WindowRequired.class)
    // @ConditionalOnWindow
    @Conditional(WindowOrLinuxRequired.class)
    @Bean
    SomeBean someBean() {
        return new SomeBean();
    }
}
```

## Kết hợp với điều kiện AND

Có thể kết hợp các điều kiện với phép AND bằng cách kế thừa lớp `AllNestedConditions`. Cách kế thừa của nó giống với `AllNestedCondition`. Ngoài ra có thể sử dụng nhiều custom `@Conditional` cùng một lúc.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/** AppConfiguration */
@Configuration
public class AppConfiguration {
    public static class SomeBean {}

    // @Conditional(WindowRequired.class)
    @ConditionalOnWindow
    // @Conditional(WindowOrLinuxRequired.class)
    @Conditional(LinuxRequired.class)
    @Bean
    SomeBean someBean() {
        return new SomeBean();
    }
}
```
