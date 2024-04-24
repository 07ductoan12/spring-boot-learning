# Content

<!--toc:start-->

- [Content](#content)
  - [Tạo Database](#tạo-database)
  - [Cấu hình ứng dụng](#cấu-hình-ứng-dụng)
  - [Tạo Model](#tạo-model)
  - [TodoConfig](#todoconfig)
  - [Tầng Repository](#tầng-repository)
  - [Tầng Service](#tầng-service)
  - [Tầng Controller](#tầng-controller)
  - [i18n](#i18n)
  - [Templates](#templates)
  - [Chạy thử ứng dụng](#chạy-thử-ứng-dụng)
  <!--toc:end-->

## Tạo Database

_script.sql_

```sql
CREATE SCHEMA IF NOT EXISTS `todo_db` DEFAULT CHARACTER SET utf8mb4 ;

CREATE TABLE IF NOT EXISTS `todo_db`.`todo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `detail` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
```

Thêm 1 record vào DB

```sql
INSERT INTO `todo_db`.`todo` (`title`, `detail`) VALUES ('Làm bài tập', 'Hoàn thiện bài viết Spring Boot #13');
```

## Cấu hình ứng dụng

_application.properties_

```java
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
# Đổi thành tiếng anh bằng cách bỏ comment ở dưới
#spring.mvc.locale=en_US

spring.datasource.url=jdbc:mysql://localhost:3306/todo_db?useSSL=false
spring.datasource.username=root
spring.datasource.password=root


## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update
```

## Tạo Model

Tạo model `Todo` liên kết với bảng `todo` trong Database (Sử dụng Lombok và Hibernate Todo.java).

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

/** Todo */
@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String detail;
}
```

đối tượng `TodoValidator`, có trách nghiệm kiểm tra xem một `object` có hợp lệ hay không.

```java
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

/** TodoValidator: kiểm tra xem một Object Todo có hợp lệ không */
public class TodoValidator {
    /**
     * Kiểm tra một object Todo có hợp lệ không
     *
     * @param todo
     * @return
     */
    public Boolean isValid(Todo todo) {
        return Optional.ofNullable(todo)
                .filter(t -> !StringUtils.isEmpty(t.getTitle())) // Kiểm tra title khác rỗng
                .filter(t -> !StringUtils.isEmpty(t.getDetail())) // kiểm tra details khác rỗng
                .isPresent(); // Trả về true nếu hợp lệ, ngược lại false.
    }
}
```

## TodoConfig

Tạo Bean cho `TodoValidator`.
_config/TodoConfig.java_

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** TodoConfig */
@Configuration
public class TodoConfig {

    @Bean
    public TodoValidator validator() {
        return new TodoValidator();
    }
}
```

## Tầng Repository

Tầng Repository có trách nghiệm giao tiếp với Database (Sử dụng **Spring JPA**)

_repository/TodoRepository.java_

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** TodoRepository */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {}
```

## Tầng Service

Tầng Service, chịu trách nghiệm thực hiện các xử lý logic, hỗ trợ cho tần Controller.

_service/TodoService.java_

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** TodoService */
@Service
public class TodoService {

    @Autowired private TodoRepository todoRepository;

    @Autowired private TodoValidator todoValidator;

    public List<Todo> findAll(Integer limit) {
        return Optional.ofNullable(limit)
                .map(value -> todoRepository.findAll(PageRequest.of(0, value)).getContent())
                .orElseGet(() -> todoRepository.findAll());
    }

    public Todo add(Todo todo) {
        if (todoValidator.isValid(todo)) {
            return todoRepository.save(todo);
        }
        return null;
    }
}
```

## Tầng Controller

Tầng Controller nơi nhận các request từ phía người dùng và chuyển xuống tầng service.

_controller/TodoController.java_

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/** TodoController */
@Controller
public class TodoController {
    @Autowired private TodoService todoService;

    /**
     * @RequestParam dùng để đánh dấu một biến là request param trong request gửi lên server. Nó sẽ
     * gán dữ liệu của param-name tương ứng vào biến
     *
     * @param model
     * @param limit
     * @return
     */
    @GetMapping("/listTodo")
    public String index(
            Model model, @RequestParam(value = "limit", required = false) Integer limit) {
        // Trả về đối tượng todoList.
        model.addAttribute("todoList", todoService.findAll(limit));
        // Trả về template "listTodo.html"
        return "listTodo";
    }

    @GetMapping("/addTodo")
    public String addTodo(Model model) {
        model.addAttribute("todo", new Todo());
        return "addTodo";
    }

    /**
     * @ModelAttribute đánh dấu đối tượng Todo được gửi lên bởi Form Request
     *
     * @param todo
     * @return
     */
    @PostMapping("/addTodo")
    public String addTodo(@ModelAttribute Todo todo) {
        return Optional.ofNullable(todoService.add(todo))
                .map(t -> "success") // Trả về success nếu save thành công
                .orElse("failed"); // Trả về failed nếu không thành công
    }
}
```

## i18n

định nghĩa các message tại thư mục `i18n`.

_i18n/messages_vi.properties_

```text
toan.message.hello=Welcome to TodoApp
toan.message.success=Thêm Todo thành công!
toan.message.failed=Thêm Todo không thành công!

toan.value.addTodo=Thêm công việc
toan.value.viewListTodo=Xem danh sách công việc
toan.value.listTodo=Danh sách công việc
```

_i18n/messages_en.properties_

```text
toan.message.hello=Welcome to TodoApp
toan.message.success=Add To-do Successfully!
toan.message.failed=Add To-do Failed!

toan.value.addTodo=Add To-do
toan.value.viewListTodo=View To-do list
toan.value.listTodo=To-do list
```

## Templates

_index.html_

```html
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Toan To-do</title>
    <!--css-->
    <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
    <link th:href="@{/css/main.css}" rel="stylesheet" />

    <!--js-->
    <script th:src="@{/js/bootstrap.js}"></script>
  </head>
  <body>
    <h1 th:text="#{toan.message.hello}"></h1>

    <a
      th:href="@{/listTodo}"
      th:text="#{toan.value.viewListTodo}"
      class="btn btn-primary"
    ></a>
    <a
      th:href="@{/addTodo}"
      th:text="#{toan.valud.addTodo}"
      class="btn btn-success"
    ></a>
  </body>
</html>
```

_listTodo.html_

```html
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Toan To-do</title>
    <!--css-->
    <link th:href="@{/css/bootstrap.css}" rel="stylesheet" />
    <link th:href="@{/css/main.css}" rel="stylesheet" />

    <!--js-->
    <script th:src="@{/js/bootstrap.js}"></script>
  </head>
  <body>
    <h1 th:text="#{toan.value.listTodo} + :"></h1>

    <ul>
      <li th:each="todo : ${todoList}">
        <span th:text="*{todo.getTitle()}"></span> :
        <span th:text="*{todo.getDetail()}"></span>
      </li>
    </ul>

    <a
      th:href="@{/addTodo}"
      th:text="#{toan.value.addTodo}"
      class="btn btn-success"
    ></a>
  </body>
</html>
```

_addTodo.html_

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
    <h1>To-do</h1>

    <form th:action="@{/addTodo}" th:object="${todo}" method="post">
      <p>title: <input type="text" th:field="*{title}" /></p>
      <p>detail: <input type="text" th:field="*{detail}" /></p>
      <p><button type="submit" class="btn btn-success">Add</button></p>
    </form>
  </body>
</html>
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
    <h1 style="color: green" th:text="#{toan.message.success}"></h1>

    <a
      th:href="@{/listTodo}"
      th:text="#{toan.value.viewListTodo}"
      class="btn btn-primary"
    ></a>
  </body>
</html>
```

_failed.html_

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
    <h1 style="color: red" th:text="#{toan.message.failed}"></h1>

    <a
      th:href="@{/listTodo}"
      th:text="#{toan.value.viewListTodo}"
      class="btn btn-primary"
    ></a>
  </body>
</html>
```

## Chạy thử ứng dụng

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
