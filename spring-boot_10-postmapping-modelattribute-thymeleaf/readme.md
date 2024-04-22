# Content

## @PostMapping

`@PostMapping` có nhiệm vụ đánh dấu hàm xử lý POST request trong Controller.

```java
@Controller
public class WebController {
    @GetMapping("/addTodo")
    public String addTodo(Model model){
        return "addTodo";
    }

    @PostMapping("/addTodo")
    public String addTodo(Model model){
        return "success";
    }
}
```

-> Cả hai đều có chung một `path` nhưng khác `method` thì sẽ xử lý khác nhau.

`PostMapping` xử lý cho method POST -> các method khác như `PUT`, `DELETE`, ... có `@PutMapping`, `@DeleteMapping`, ... để xử lý.

## @RequestMapping

Trong trường hợp tất cả các method đều dùng chung một cách xử lý thì sử dụng Annotation `@RequestMapping`. `@RequestMapping` là một annotation có ý nghĩa và mục đích sử dụng rộng hơn các loại `@GetMapping`, `@PostMapping`,...

Ví dụ:

```java
@Controller
@RequestMapping("api/v1")
public class WebController {
    // Đường dẫn lúc này là: /api/v1/addTodo và method GET
    @RequestMapping(value = "/addTodo", method = RequestMethod.GET)
    public String addTodo(Model model) {
        return "addTodo";
    }

    // Đường dẫn lúc này là: /api/v1/addTodo và method POST
    @RequestMapping(value = "/addTodo", method = RequestMethod.POST)
    public String addTodo(@ModelAttribute Todo todo) {
        return "success";
    }
}
```

## Ví dụ (Website To-do)

### Tạo model

_Todo.java_

```java
import lombok.Data;

/** Todo */
@Data
public class Todo {
    public String title;
    public String detail;
}
```

### GET /listTodo - lấy danh sách các việc cần làm

Method cho đường dẫn `/listTodo` là `GET`.
_WebController.java_

```java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** WebController */
@Controller
public class WebController {
    List<Todo> todoList = new CopyOnWriteArrayList<>();

    @GetMapping("/listTodo")
    public String index(
            Model model, @RequestParam(value = "limit", required = false) Integer limit) {
        model.addAttribute("todoList", limit != null ? todoList.subList(0, limit) : todoList);
        return "listTodo";
    }
}
```

Chú thích **Request Param**: ví dụ khi request lên server `http://localhost:8080/listTodo?limit=2` thì đoạn `?limit=2` là **RequestParam**. **Spring Boot** sẽ tự xử lý và gán số 2 vào biến `Integer limit`. Nếu giá trị `limit` không được gửi lên thì `limit` sẽ là `null`.

### Get /addTodo - Trang thêm công việc

`GET /addTodo` sẽ trả về webpage cho người dùng nhập thông tin và trả về danh sách.

_WebController.java_

```java
@GetMapping("/addTodo")
    public String addTodo(Model model) {
        // Thêm mới một đối tượng Todo vào model
        model.addAttribute("todo", new Todo());
        // Trả về template addTodo.html
        return "addTodo";
    }
```

`GET /addTodo` sẽ trả về webpage cho người dùng nhập thông tin công việc và thêm vào danh sách việc cần làm.

_addTodo.html_

```html
<!doctype html>
<html lang="en" xmlns:th="thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Toan to-do</title>
    <!--css-->
    <link href="@{/css/bootstrap.css}" rel="stylesheet" />
    <link th:href="@{/css/main.css}" rel="stylesheet" />

    <!--js-->
    <script th:src="@{/js/bootstrap.js}"></script>
  </head>
  <body>
    <h1>To-do</h1>

    <form th:action="@{/addTodo}" th:object="${todo}" method="post">
      <p>title: <input type="text" th:field="*{title}" /></p>
      <p>detail: <input type="text" th:field="*{detail}" /></p>
      <p><button type="submit" class="btn btn-success">Add</button></p>
    </form>
  </body>
</html>
```

Thẻ `<form></form>` tạo Request From gửi tới server với thông tin và các thẻ `<input>` trong nó. file `addTodo.html` gán vào `Model` một đối tượng `todo`:

- Trong `form`, lấy ra đối tượng `Todo` và chỉ định bởi `th:object=${todo}`.
- Gán thông tin người dùng nhập vào `Todo` bằng cú pháp `th:field=*{tên_thuộc_tính}`.
- Bấm nút `Button` thì `form` sẽ gửi request `POST` có chứa `Todo` lên địa chỉ `/addTodo`.

### POST /addTodo - Thêm công việc vào list

_WebController.java_

```java
/*
    @ModelAttribute đánh dấu đối tượng Todo được gửi lên bởi Form Request
     */
    @PostMapping("/addTodo")
    public String addTodo(@ModelAttribute Todo todo) {
        // Thêm đối tượng todo vào list
        todoList.add(todo);
        // Trả về trang thành công success.html
        return "success";
    }
```

_success.html_

```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <title>Toan To-do</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
    <link th:href="@{/css/main.css}" rel="stylesheet" />
  </head>
  <body>
    <h1>To-do</h1>
    <h1>Thêm thành công!</h1>

    <a th:href="@{/listTodo}" class="btn btn-primary"
      >Xem danh sách công việc</a
    >
  </body>
</html>
```

### Chạy trương trình

_App.java_

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** App */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

_index.html_

```html
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <title>Toan To-do</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <!--css-->
    <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
    <link th:href="@{/css/main.css}" rel="stylesheet" />

    <!--js-->
    <script th:src="@{/js/bootstrap.js}"></script>
  </head>
  <body>
    <h1>Todo App</h1>

    <a th:href="@{/listTodo}" class="btn btn-primary">Xem danh sách</a>
    <a th:href="@{/addTodo}" class="btn btn-success">+ Thêm công việc</a>
  </body>
</html>
```

Truy cập ứng dụng: `http://localhost:8080/`.
Bấm **Xem danh sách**: để gửi request `GET /listTodo`.
Bấm **Thêm công việc**: để gửi request `GET /addTodo`.
Nhập thông tin và bấm vào **Add** để gửi request `POST /addTodo`.
Xem lại danh sách công việc bằng **Xem danh sách công việc**.
Giới hạn danh sách hiển thị bằng cách truyền param `limit` (`http://localhost:8080/listTodo?limit=2`).
