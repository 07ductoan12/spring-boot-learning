# Content

<!--toc:start-->

- [Content](#content)
  - [Thymeleaf](#thymeleaf)
  - [Cú pháp](#cú-pháp)
  - [Model và View Trong Spring Boot](#model-và-view-trong-spring-boot)
  - [Ví dụ](#ví-dụ)
    - [${...} - Variables Expressions](#variables-expressions)
    - [{...} - Message Expression](#message-expression)
    - [@{...} - URL Expression](#url-expression)
  - [Demo](#demo) - [Cấu hình thymeleaf](#cấu-hình-thymeleaf) - [Chức năng message](#chức-năng-message) - [Static & Template](#static-template) - [Index.html](#indexhtml) - [Thêm @Controller cho path /profile](#thêm-controller-cho-path-profile)
  <!--toc:end-->

## Thymeleaf

**Thymeleaf** là một java Template Engine. Có nhiệm vụ xử lý và generate ra các file HTML, XML, ... Các file HTML do Thymeleaf tạo ra là nhờ kết hợp **dữ liệu** và **template + quy tắc** để sinh ra một file HTML chứa đầy đủ thông tin.
Người dùng cung cấp dữ liệu và quy định **template** như nào, còn việc dùng các thông tin đó để tạo ra một file HTML do **Thymeleaf** giải quyết.

## Cú pháp

Cú pháp của **Thymeleaf** sẽ là một **attributes** (Thuộc tính) của thẻ HTML và bắt đầu bằng chữ `th:`.
ví dụ:
để truyền dữ liệu từ biến `name` trong Java vào một thẻ `H1` của HTML.

```html
<h1 th:text="${name}"</h1>
```

Cú pháp viết thẻ H1 như bình thường, nhưng không chứa text trong thẻ. Sử dụng cú pháp `th:text=${name}` để **Thymeleaf** lấy thông tin từ biến `name` và đưa nó vào thẻ `h1`.

```
// với String name = "toan"
<h1>toan</h1>
```

thuộc tính `th:text` biến mất và giá trị biến `name` được đưa vào thẻ `h1`.

## Model và View Trong Spring Boot

`Model` là đối tượng lưu trữ thông tin và được sử dụng bởi **Template Engine** để generate ra webpage. Có thể hiểu nó là `Context` của **Thymeleaf**.
`Model` lưu trữ thông tin dưới dạng key-value.

Trong template thymeleaf, để lấy các thông tin trong `Model` sử dụng `Thymeleaf Standard Expression`.

1. `${...}`: Giá trị của một biến.
2. `*{...}`: Giá trị của một biến được chỉ định.
3. `#{...}`: Lấy message.
4. `@{...}`: Lấy đường dẫn URL dựa theo context của server.

## Ví dụ

### ${...} - Variables Expressions

Trên Controller đưa vào một số giá trị:

```java
model.addAttribute("today", "Monday");
```

Để lấy giá trị của biến `today`, sử dụng `${...}`

```html
<p>Today is: <span th:text="${today}"></span></p>
```

Đoạn expression trên tương đương với:

```java
ctx.getVariable("today");
```

Dấu `*` được gọi là `asterisk syntax`. Giống với `${...}` là lấy giá trị của một biến. Điểm khác biệt là nó sẽ lấy ra giá trị của một biến cho trước bởi `th:object`.

```html
<div th:object="${session.user}">
  <!-- th:object tồn tại trong phạm vi của thẻ div này -->

  <!-- Lấy ra tên của đối tượng session.user -->
  <p>Name: <span th:text="*{firstName}"></span>.</p>
  <!-- Lấy ra lastName của đối tượng session.user -->
  <p>Surname: <span th:text="*{lastName}"></span>.</p>
</div>
```

còn `${...}` sẽ lấy giá trị cục bộ trong `Context` hay `Model`.

Đoạn code trên tương đương với:

```html
<div>
  <p>Name: <span th:text="${session.user.firstName}"></span>.</p>
  <p>Surname: <span th:text="${session.user.lastName}"></span>.</p>
</div>
```

### #{...} - Message Expression

Ví dụ, trong file config .properties có một message chào người dùng

```
home.welcome=¡Bienvenido a nuestra tienda de comestibles!
```

Cách lấy nó ra nhanh nhất

```html
<p th:utext="#{home.welcome}">Xin chào các bạn!</p>
```

### @{...} - URL Expression

`@{...}` xử lý và trả ra giá trị URL theo context của máy chủ.

Ví dụ:

```html
<!-- tương đương với 'http://localhost:8080/order/details?orderId=3' -->
<a
  href="details.html"
  th:href="@{http://localhost:8080/order/details(orderId=${o.id})}"
  >view</a
>

<!-- tương đương  '/order/details?orderId=3' -->
<a href="details.html" th:href="@{/order/details(orderId=${o.id})}">view</a>

<!-- tương dương '/gtvg/order/3/details' -->
<a href="details.html" th:href="@{/order/{orderId}/details(orderId=${o.id})}"
  >view</a
>
```

Nếu bắt dầu bằng dấu `/` thì nó sẽ là Relative URL và sẽ tương ứng theo context của máy chủ.

## Demo

### Cấu hình thymeleaf

_application.properties_

```text
#Chạy ứng dụng trên port 8085
server.port=8085

# Bỏ tính năng cache của thymeleaf để lập trình cho nhanh
spring.thymeleaf.cache=false

# Các message tĩnh sẽ được lưu tại thư mục i18n
spring.messages.basename=i18n/messages


# Bỏ properties này đi khi deploy
# Nó có tác dụng cố định ngôn ngữ hiện tại chỉ là Tiếng Việt
spring.mvc.locale-resolver=fixed

# Mặc định ngôn ngữ là tiếng việt
spring.mvc.locale=vi_VN
# Đổi thành tiếng anh bằng cách bỏ comment ở dứoi
#
```

### Chức năng message

Các trang website hỗ trợ đa ngôn ngữ (i18n) thì các message sẽ được lưu dưới dạng key-value. Và tùy theo từng địa lý mà chọn sử dụng value cho hợp lý.

**Thymeleaf** sẽ làm điều này, ở trên đã cấu hình (file `_application.properties_`).

_i18n/messages_vi.properties_

```
loda.hello=Xin chào tất cả các bạn tới với Loda Website
```

_i18n/messages_en.properties_

```
loda.hello=Welcome to Loda Website
```

### Static & Template

Các file `css` và `javascript` sẽ được lưu lại trong thư mục `resources/static`.
File `.html` là dạng template sử dụng để render ra webpage và trả về cho người dùng. Nó được lưu lại tại thư mục `resources/templates`.
Thymeleaf sẽ tự biết tìm đến những tài nguyên này.

### Index.html

`index.html` sẽ là file mặc định mà **Thymeleaf** tìm đầu tiên và trả về mỗi khi người dùng vào địa chỉ `/` hay `https://localhost:8085/` mà không cần config.

Trong `index.html`:

1. Gọi ra `bootstrap.css` và `bootstrap.js` trong thư mục `resources/static` bằng expression `@{...}`.
2. Hiển thị dòng chữ chào `toan.hello` trong thư mục `resources/i18n` bằng expression `#{...}`.

_index.html_

```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <title>Loda</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <!--css-->
    <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />

    <!--js-->
    <script th:src="@{/js/bootstrap.js}"></script>
  </head>
  <body>
    <h1 th:utext="#{toan.hello}"></h1>

    <a th:href="@{/profile}" class="btn btn-primary">Toan Profile</a>
  </body>
</html>
```

hàm chạy thử:

_App.java_

```java
/** App */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

### Thêm @Controller cho path /profile

_Info.java_

```java
import lombok.AllArgsConstructor;
import lombok.Data;

/** Info */
@Data
@AllArgsConstructor
public class Info {

    String key;
    String value;
}
```

_WebController.java_

```java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/** WebController */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        List<Info> profile = new ArrayList<>();

        profile.add(new Info("fullname", "Nguyễn Đức Toàn"));
        profile.add(new Info("gmail", "07ductoan12@gmail.com"));
        profile.add(
                new Info("facebook", "https://www.facebook.com/profile.php?id=100005369418888"));

        model.addAttribute("toanProfile", profile);
        return "profile";
    }
}
```

_profile.html_

```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <title>Loda</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <!--css-->
    <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />

    <!--js-->
    <script th:src="@{/js/bootstrap.js}"></script>
  </head>
  <body>
    <h1 th:utext="#{toan.hello}"></h1>
    <h2>Loda Profile</h2>

    <ul>
      <!--Duyệt qua toàn bộ phần tử trong biến "toanProfile"-->
      <li th:each="info : ${toanProfile}">
        <!--Với mỗi phần tử, lấy ra key và value-->
        <span th:text="*{info.key}"></span> :
        <span th:text="*{info.value}"></span>
      </li>
    </ul>
  </body>
  <
</html>
```
