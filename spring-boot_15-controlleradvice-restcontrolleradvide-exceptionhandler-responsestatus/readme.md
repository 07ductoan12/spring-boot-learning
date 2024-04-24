# Content

<!--toc:start-->

- [Content](#content)
  - [@RestControllerAdvice & @ControllerAdvice + @ExceptionHandler](#restcontrolleradvice-controlleradvice-exceptionhandler)
  - [@ResponseStatus](#responsestatus)
  - [Demo](#demo) - [Tạo model](#tạo-model) - [Tạo Controller](#tạo-controller) - [Tạo Exception Handler](#tạo-exception-handler) - [Chạy ứng dụng](#chạy-ứng-dụng)
  <!--toc:end-->

## @RestControllerAdvice & @ControllerAdvice + @ExceptionHandler

`@RestControllerAdvice` là một Annotation gắn trên Class. Có tác dụng xen vào quá trình xử lý các `@RestController`. Tương tự với `@ControllerAdvice`.
`@RestControllerAdvice` thường được kết hợp với `@ExceptionHandler` để cắt quá trình xử lý của controller, và được xử lý ngoại lên xảy ra.

```java
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage TodoException(Exception ex,  WebRequest request) {
        return new ErrorMessage(10100, "Đối tượng không tồn tại");
    }
}
```

-> Nếu chẳng may có Exception được ném ra, thì thay vì hệ thống thông báo lỗi nó sẽ được `@RestControllerAdvice` và `@ExceptionHandler` đón lấy xử lý. Sau đó trả về kết quả cho người dùng.

## @ResponseStatus

`@ResponseStatus` là một cách định nghĩa Http Status trả về. Nếu không muốn sử dụng `ResponseEntity` thì có thể dùng `@ResponseStatus` đánh dấu trên `Object` trả về.

## Demo

### Tạo model

_Todo.java_

```java

import lombok.AllArgsConstructor;
import lombok.Data;

/** Todo */
@Data
@AllArgsConstructor
public class Todo {
    private String todo;
    private String details;
}
```

_ErrorMessage.java_

```java
import lombok.AllArgsConstructor;
import lombok.Data;

/** ErrorMessage */
@Data
@AllArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private String message;
}
```

### Tạo Controller

_RestApiController.java_

```java
import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** RestApiController */
@RestController
@RequestMapping("/api/v1")
public class RestApiController {
    private List<Todo> todoList;

    @PostConstruct
    public void init() {
        todoList =
                IntStream.range(0, 10)
                        .mapToObj(i -> new Todo("title-" + i, "detail-" + i))
                        .collect(Collectors.toList());
    }

    @GetMapping("/todo/{todoId}")
    public Todo getTodo(@PathVariable(name = "todoId") Integer todoId) {
        // @PathVariable lấy ra thông tin trong URL
        // dựa vào tên của thuộc tính đã định nghĩa trong ngoặc kép /todo/{todoId}
        return todoList.get(todoId);
    }
}
```

### Tạo Exception Handler

_ApiExceptionHandler.java_

```java
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/** ApiExceptionHandler */
@RestControllerAdvice
public class ApiExceptionHandler {
    /**
     * @RestControllerAdvice(value = HttpStatus.INTERNAL_SERVER_ERROR)
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handlerAllException(Exception ex, WebRequest request) {
        return new ErrorMessage(10000, ex.getLocalizedMessage());
    }

    /**
     * IndexOutOfBoundsException sẽ được xử lý riêng tại đây
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage TodoException(Exception ex, WebRequest request) {
        return new ErrorMessage(10100, "Đối tượng không tồn tại");
    }
}
```

### Chạy ứng dụng

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

Api test

```http
GET http://localhost:8080/api/v1/todo/11
```
