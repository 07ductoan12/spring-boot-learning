# Content

<!--toc:start-->

- [Content](#content)
  - [Cấu hình đơn giản](#cấu-hình-đơn-giản)
  - [Chạy thử](#chạy-thử)
  - [Nested Properties](#nested-properties)
  <!--toc:end-->

## Cấu hình đơn giản

Giả sử có một số giá trị toàn cục, thay vì cấu hình trong code -> lưu nó ở bên ngoài.

Ví dụ: Tạo ra một class chứa các thông tin

```java
@Data // Lombok, xem chi tiết tại bài viết
@Component // Là 1 spring bean
// @PropertySource("classpath:toan.yml") // Đánh dấu để lấy config từ trong file toan.yml
@ConfigurationProperties(prefix = "toan") // Chỉ lấy các config có tiền tố là "toan"
public class ToanAppProperties {
    private String email;
    private String googleAnalyticsId;

    // standard getters and setters
}
```

Sử dụng `@Component` để Spring biết đây là một bean và khởi tạo nó. Sử dụng `@PropertySource` để định nghĩa tên của file config. Nếu không có annotation này, Spring sẽ sử dụng mặc định (classpath:application.yml trong thư mục resources).
`@ConfigurationProperties` annotation này đánh dấu class bên dưới nó là một properties, các thuộc tính sẽ được tự động nạp vào khi Spring khởi tạo. (Lưu ý: các thuộc tính này được xác định bởi `prefix=toan`).
Spring sẽ tự tìm các hàm setter để set giá trị cho các thuộc tính này, nên quan trọng phải tạo ra các setter method (Có thể để nó cho _lombok_).

Ngoài ra, để chạy được tính năng này, cần kích hoạt nó bằng cách `@EnableConfigurationProperties` lên một configuration nào đó. Có thể gắn lên main:

```java
@SpringBootApplication
@EnableConfigurationProperties
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

Bây giờ, **Spring sẽ tự động bind toàn bộ giá trị từ trong file application.yml vào bean LodaAppProperties cho chúng ta**.

Tạo file _application.yml_ tại thư mục resources:

```yml
toan:
  email: 07ductoan12@gmail.com
  googleAnalyticsId: U-xxxxx
```

## Chạy thử

_App.java_

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/** App */
@SpringBootApplication
@EnableConfigurationProperties
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired ToanAppProperties toanAppProperties;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Global variable:");
        System.out.println("\t Email: " + toanAppProperties.getEmail());
        System.out.println("\t GA ID: " + toanAppProperties.getGoogleAnalyticsId());
    }
}
```

kết quả:

```text
Global variable:
         Email: 07ductoan12@gmail.com
         GA ID: U-xxxxx
```

Bây giờ, ở bất kỳ đâu trong chương trình, khi cần lấy các thông tin config chỉ cần:

```java
@Autowired ToanAppProperties toanAppProperties;
```

## Nested Properties

Có thể config các thuộc tính bên trong Class kể cả khi nó là `Lists`, `Maps` hay một class khác.
Bổ xung thêm thuộc tính:

```java
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/** ToanAppProperties */
@Data
@Component // Là 1 spring bean
// @PropertySource("classpath:toan.yml") // Đánh dấu để lấy config từ trong file toan.yml
@ConfigurationProperties(prefix = "toan") // Chỉ lấy các config có tiền tố là "toan"
public class ToanAppProperties {
    private String email;
    private String googleAnalyticsId;
    private List<String> authors;
    private Map<String, String> exampleMap;
}
```

sửa file _application.yml_

```yml
toan:
  email: 07ductoan12@gmail.com
  googleAnalyticsId: U-xxxxx
  authors:
    - toan
    - atom
  exampleMap:
    key1: hello
    key2: world
```

chạy chương trình:

```java
@Override
    public void run(String... args) throws Exception {
        System.out.println("Global variable:");
        System.out.println("\t Email: " + toanAppProperties.getEmail());
        System.out.println("\t GA ID: " + toanAppProperties.getGoogleAnalyticsId());
        System.out.println("\t Authors: " + toanAppProperties.getAuthors());
        System.out.println("\t Example Map: " + toanAppProperties.getExampleMap());
    }
```

Kết quả:

```text
Global variable:
         Email: 07ductoan12@gmail.com
         GA ID: U-xxxxx
         Authors: [toan, atom]
         Example Map: {key1=hello, key2=world}
```
