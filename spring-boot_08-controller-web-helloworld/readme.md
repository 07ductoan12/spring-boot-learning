# Content

## @Controller

`@Controller` là nơi tiếp nhận các thông tin request từ phía người dùng. Nhiệm vụ chính là tiếp nhận yêu cầu kèm theo các thông tin request và chuyển các yêu cầu này xuống cho tầng `@Service`.

## HTML

Mặc định trong **Spring Boot**, các file html này sẽ được lưu trữ trong thư mục `resources/templates`.

## Ví dụ 1

_WebController.java_

```java
/** WebController */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
}
```

_index.html_

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Hello World</title>
  </head>
  <body>
    <h1>Đây là một trang web</h1>
  </body>
</html>
```

chạy chương trình:
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

## Giải thích vd 1

Bản thân `@Controller` cũng là một `@Component`. **Spring Boot** sẽ lắng nghe các request từ phía người dùng và tùy theo đường dẫn `path` **Spring Boot** sẽ mapping tới hàm xử lý tương ứng trong `@Controller`.

Ví dụ: địa chỉ host mặc định của **Spring Boot** tại địa chỉ `localhost:8080`, đường dẫn mapping là `/` (`@GetMapping("/")`). Khi gửi địa yêu cầu đến đường dẫn `localhost:8080/` và yêu cầu hàm xử lý request này -> **Spring Boot** trả về một file index.html.

## Ví dụ 2

mở rộng `WebController`:

```java
/** WebController */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/hello")
    public String hello(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            Model model) {
        model.addAttribute("name", name);
        return "hello";
    }
}
```

_index.html_

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Hello World</title>
  </head>
  <body>
    <h1>Đây là một trang web</h1>
    <a href="/about">About</a>
    <form method="get" action="/hello">
      <input type="input" name="name" />
      <button type="submit">Submit</button>
    </form>
  </body>
</html>
```

_about.html_

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Hello World</title>
  </head>
  <body>
    <h1>Toan</h1>
    <a href="https://github.com/07ductoan12/spring-boot-learning">Github</a>
  </body>
</html>
```

_hello.html_

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Hello World</title>
  </head>
  <body>
    <h1 th:text="'Hello, ' + ${name}"></h1>
    <a href="/">Trang chủ</a>
  </body>
</html>
```

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

Khi nhập tên vào ô và submit. Nó sẽ tạo một request GET tới địa chỉ /hello kèm theo param `?name=your_name`.

## Giải thích

```java
@GetMapping("/hello")
    public String hello(
            // Request param "name" sẽ được gán giá trị vào biến String
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            // Model là một object của Spring Boot, được gắn vào trong mọi request.
            Model model
    ) {
        // Gắn vào model giá trị name nhận được
        model.addAttribute("name", name);

        return "hello"; // trả về file hello.html cùng với thông tin trong object Model
    }
```

Khi request lên, ta nhận được giá trị của `name` và tiếp tục gán nó vào `Model`.
`Model` ở đây là một object được **Spring Boot** đính kèm trong mỗi response.
`Model` chứa thông tin muốn trả về và **Template Engine** sẽ trích xuất thông tin này thành html đưa cho người dùng.
trong file `hello.html` giá trị của `name` trong `Model` được sử dụng với cú pháp của `Thymeleaf`.

```html
<h1 th:text="'Hello, ' + ${name}"></h1>
```
