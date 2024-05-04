# Content

Như mặc định, Spring sẽ tạo ra tất cả các `singleton Bean` trong quá trình startup `Application Context`. Tuy nhiên, có những trường hợp chưa dùng đến `Bean` khi mới startup `Application Context` mà khi yêu cầu mới được tạo. Spring cung cấp annotation `@Lazy`.

## @Lazy và @Configuration

Tạo các `Bean` cơ bản:

_FirstBean.java_

```java
/** FirstBean */
public class FirstBean {

    public FirstBean() {
        System.out.println("Bean FirstBean đã được khởi tạo");
    }
}
```

_SecondBean.java_

```java
/** SecondBean */
public class SecondBean {

    public SecondBean() {
        System.out.println("Bean SecondBean đã được khởi tạo");
    }
}
```

_ApplicationConfig.java_

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(App.class);

        System.out.println("ApplicationContext đã khởi tạo");
        FirstBean firstBean = context.getBean(FirstBean.class);
        SecondBean secondBean = context.getBean(SecondBean.class);
        context.close();
    }
}
```

Output:

```text
ApplicationContext đã khởi tạo
Bean FirstBean đã được khởi tạo
Bean SecondBean đã được khởi tạo
```

Khi đặt annotation `@Lazy` trong class `@Configuration`, nó chỉ ra rằng các phương thức với annotation `@Bean` sẽ tạm thời chưa được khởi tạo `ApplicationContext` startup. Sau khi start xong, được yêu cầu getBean() thì chúng mới bắt đầu được khởi tạo.

## @Lazy và @Bean

Để áp dụng vào `@Bean` được chỉ định riêng, chỉ cần đặt annotation `@Lazy` vào `Bean` được yêu cầu.
_ApplicationConfig.java_

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/** ApplicationConfig */
@Configuration
public class ApplicationConfig {

    @Lazy
    @Bean
    public FirstBean firstBean() {
        return new FirstBean();
    }

    @Bean
    public SecondBean secondBean() {
        return new SecondBean();
    }
}
```

Output:

```text
Bean SecondBean đã được khởi tạo
ApplicationContext đã khởi tạo
Bean FirstBean đã được khởi tạo
```

## @Lazy và @Component

Thay vì tạo `Bean` trong class `config`, có thể khởi tạo annotation `@Component`. Khi đó, tương tự kết hợp `@Lazy` và `@Bean`.

_FirstBean.java_

```java
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** FirstBean */
@Lazy
@Component
public class FirstBean {

    public FirstBean() {
        System.out.println("Bean FirstBean đã được khởi tạo");
    }
}
```

## @Lazy và @Autowired

Trong trường hợp này `@Lazy` được đặt cả ở `@Component` và `@Autowired`.

_ServiceBean.java_

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/** ServiceBean */
public class ServiceBean {

    @Lazy @Autowired private FirstBean firstBean;

    public FirstBean getFirstBean() {
        return firstBean;
    }
}
```

khi đó bean sẽ được tạo khi gọi hàm `getFirstBean()`.
