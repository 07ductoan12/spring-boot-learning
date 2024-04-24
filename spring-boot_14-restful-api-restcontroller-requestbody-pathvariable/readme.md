# Content

<!--toc:start-->

- [Content](#content)
  - [@RestController](#restcontroller)
  - [@RequestBody](#requestbody)
  - [@PathVariable](#pathvariable)
  - [Demo](#demo) - [Tạo model](#tạo-model) - [Tạo RestController](#tạo-restcontroller) - [Chạy app](#chạy-app)
  <!--toc:end-->

## @RestController

Khác với `@Controller` sẽ trả về một template.
`@RestController` trả về dữ liệu dưới dạng JSON.

```java
@RestController
@RequestMapping("/api/v1")
public class RestApiController{

    @GetMapping("/todo")
    public List<Todo> getTodoList() {
        return todoList;
    }
}
```

Các đối tượng trả về dưới dạng Object sẽ được **Spring Boot** chuyển thành Json. Các đối tượng trả về đa dạng như `List`, `Map`, v.v. **Spring Boot** sẽ convert hết chúng về JSON, mặc định dùng Jackson converter.
Nếu muốn API tùy biến được dữ liệu trả về có thể trả về đối tượng `ResponseEntity` của **Spring** (đây là đối tượng cha của mọi response và sẽ wrapper các object trả về).

## @RequestBody

ví dụ:

```java
@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    List<Todo> todoList = new CopyOnWriteArrayList<>();

    @PostMapping("/todo")
    public ResponseEntity addTodo(@RequestBody Todo todo) {
        todoList.add(todo);
        // Trả về response với STATUS CODE = 200 (OK)
        // Body sẽ chứa thông tin về đối tượng todo vừa được tạo.
        return ResponseEntity.ok().body(todo);
    }

}
```

## @PathVariable

`RESTful API` là một tiêu chuẩn trong việc thiết kế các thiết kế API cho các ứng dụng web để quản lý các resource.
Ví dụ:

1. URL tạo To-do: http://localhost:8085/todo -> Tương ứng với HTTP method POST.
2. URL lấy thông tin To-do số 12: http://localhost:8085/todo/12 -> Tương ứng với HTTP method GET.
3. URL sửa thông tin To-do số 12: http://localhost:8085/todo/12 -> Tương ứng với HTTP method PUT.
4. URL xóa To-do số 12: http://localhost:8085/todo/12 -> Tương ứng với HTTP method DELETE.

-> Ngoài lấy thông tin trong `Body` của request, còn cần con số 12 nằm trong URL. -> Sử dụng `@PathVariable`.

Ví dụ:

```java
@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    /*
    phần path URL bạn muốn lấy thông tin sẽ để trong ngoặc kép {}
     */
    @GetMapping("/todo/{todoId}")
    public Todo getTodo(@PathVariable(name = "todoId") Integer todoId){
        // @PathVariable lấy ra thông tin trong URL
        // dựa vào tên của thuộc tính đã định nghĩa trong ngoặc kép /todo/{todoId}
        return todoList.get(todoId);
    }
}
```

## Demo

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

### Tạo RestController

_RestApiController.java_

```java
import jakarta.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Lưu ý, @RequestMapping ở class, sẽ tác động tới tất cả các RequestMapping ở bên trong nó.
 *
 * <p>Mọi Request ở trong method sẽ được gắn thêm prefix /api/v1
 */
@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    private List<Todo> todoList = new CopyOnWriteArrayList<>();

    // bạn còn nhớ @PostConstruct dùng để làm gì chứ?
    // nếu không nhớ, hãy coi lại bài viết Spring Boot #3 nhé
    @PostConstruct
    public void init() {
        // Thêm null vào List để bỏ qua vị trí số 0;
        todoList.add(null);
    }

    @GetMapping("/todo")
    public List<Todo> getTodoList() {
        return todoList;
    }

    /*
    phần path URL bạn muốn lấy thông tin sẽ để trong ngoặc kép {}
     */
    @GetMapping("/todo/{todoId}")
    public Todo getTodo(@PathVariable(name = "todoId") Integer todoId) {
        // @PathVariable lấy ra thông tin trong URL
        // dựa vào tên của thuộc tính đã định nghĩa trong ngoặc kép /todo/{todoId}
        return todoList.get(todoId);
    }

    /*
    @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
    thành đối tượng Todo
     */
    @PutMapping("/todo/{todoId}")
    public Todo editTodo(@PathVariable(name = "todoId") Integer todoId, @RequestBody Todo todo) {
        todoList.set(todoId, todo);
        // Trả về đối tượng sau khi đã edit
        return todo;
    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity deleteTodo(@PathVariable(name = "todoId") Integer todoId) {
        todoList.remove(todoId.intValue());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/todo")
    public ResponseEntity addTodo(@RequestBody Todo todo) {
        todoList.add(todo);
        // Trả về response với STATUS CODE = 200 (OK)
        // Body sẽ chứa thông tin về đối tượng todo vừa được tạo.
        return ResponseEntity.ok().body(todo);
    }
}
```

### Chạy app

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

**Tạo ra một đối tượng To-do**

```http
POST http://localhost:8080/api/v1/todo
Content-Type: application/json
{
    "title": "test1",
    "detail": "test1"
}
```

**Xem danh sách To-do**

```http
GET http://localhost:8080/api/v1/todo
```

**Sửa To-do**

```http
PUT http://localhost:8080/api/v1/todo/1
Content-Type: application/json
{
    "title": "test0",
    "detail": "test0"
}
```

**Lấy thông tin To-do**

```http
GET http://localhost:8080/api/v1/todo/1
```

**Xóa To-do**

```http
DELETE http://localhost:8080/api/v1/todo/1
```
