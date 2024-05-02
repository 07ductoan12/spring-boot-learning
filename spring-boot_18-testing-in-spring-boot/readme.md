#Content

## Vấn đề Test + Spring

**Spring boot** sẽ phải cải tạo Context và tìm kiếm các Bean và nhét vào nó. Sau tất cả các bước config và khởi tạo để sử dụng `@Autowired` để lấy ra đối tượng.
Vấn đề đầu tiên khi nghĩ tới viết Test sẽ là làm sao `@Autowired` bean vào class Test được làm sao cho `JUnit` hiểu `@Autowired`.

## @RunWith(SpringRunner.class)

**Spring Boot** đã thiết kế lớp `SpringRunner` -> sử dụng tích hợp **Spring + JUnit**.
Để test trong Spring, trong mọi class Test cần nhúng thêm `@RunWith(SpringRunner.class)` lên đầu Class Test.

```java
@RunWith(SpringRunner.class)
public class TodoServiceTest{
    ...
}
```

khi chạy `TodoServiceTest` sẽ tạo ra một `Context` riêng để chứa `bean` trong đó -> có thể `@Autowired` thoải mái trong nội hàm này.
**Vấn đề tiếp theo**: là làm sao đưa `Bean` vào trong `Context`. Có 2 cách:

1. `@SpringBootTest`
2. `@TestConfiguration`

## @SpringBootTest

`@SpringBootTest` sẽ đi tìm kiếm class có gắn `@SpringBootApplication` và từ đó toàn độ `Bean` và nạp vào `Context`. Chỉ nên sử dụng trong trường hợp muốn **Integaration Test**, vì sẽ tạo toàn bộ `Bean`, không khác gì chạy `SpringApplication.run(App.class, args);`, tốn thời gian và rất nhiều `Bean` thừa thãi.

```java

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoServiceTest {

    /**
     * Cách này sử dụng @SpringBootTest
     * Nó sẽ load toàn bộ Bean trong app của bạn lên,
     * Giống với việc chạy App.java vậy
     */

    @Autowired
    private TodoService todoService;
}
```

## @TestConfiguration

`@TestConfiguration` giống với `@Configuration`, ta tự định nghĩa ra `Bean`. Các Bean được tạo bởi `@TestConfiguration` chỉ tồn tại trong môi trường Test. Rất phù hợp với việc UnitTest. Class Test nào, cần Bean gì thì tự tạo ra trong `@TestConfiguration`.

```java
@RunWith(SpringRunner.class)
public class TodoServiceTest2 {

    /**
     * Cách này sử dụng @TestConfiguration
     * Nó chỉ tạo ra Bean trong Context test này mà thôi
     * Tiết kiệm thời gian hơn khi sử dụng @SpringBootTest (vì phải load hết Bean lên mà không dùng đến)
     */
    @TestConfiguration
    public static class TodoServiceTest2Configuration{

        /*
        Tạo ra trong Context một Bean TodoService
         */
        @Bean
        TodoService todoService(){
            return new TodoService();
        }
    }

    @Autowired
    private TodoService todoService;
}
```

## @MockBean

**Spring** hỗ trợ mock với annotation `@MockBean`, có thể mock lấy ra một `Bean` "giả" mà không thèm để ý với thằng `Bean` "thật".

```java
@RunWith(SpringRunner.class)
public class TodoServiceTest2 {

    /**
     * Đối tượng TodoRepository sẽ được mock, chứ không phải bean trong context
     */
    @MockBean
    TodoRepository todoRepository;
}
```

## Demo1

### Tạo Model, Service, Repository

_Todo.java_

```java
/** Todo */
@Data
@AllArgsConstructor
public class Todo {
    private int id;
    private String title;
    private String detail;
}
```

_TodoRepository.java_

```java
import java.util.List;

/** TodoRepository */
public interface TodoRepository {
    List<Todo> findAll();

    Todo findById(int id);
}
```

_TodoService.java_

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** TodoService */
@Service
public class TodoService {
    @Autowired TodoRepository todoRepository;

    public int countTodo() {
        return todoRepository.findAll().size();
    }

    public Todo getTodo(int id) {
        return todoRepository.findById(id);
    }

    public List<Todo> getAll() {
        return todoRepository.findAll();
    }
}
```

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

### Test bằng @SpringBootTest

Sử dụng `mock TodoRepository` và giả lập cho nó trả ra một `List<Todo>` gồm 10 phần tử.

```java
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

    /**
     * Cách này sử dụng @SpringBootTest
     * Nó sẽ load toàn bộ Bean trong app của bạn lên,
     * Giống với việc chạy App.java vậy
     */

    /**
     * Đối tượng TodoRepository sẽ được mock, chứ không phải bean trong context
     */

/** TodoServiceTest */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TodoServiceTest {
    @MockBean TodoRepository todoRepository;

    @Autowired private TodoService todoService;

    @Before
    public void setup() {
        Mockito.when(todoRepository.findAll())
                .thenReturn(
                        IntStream.range(0, 10)
                                .mapToObj(i -> new Todo(i, "title-" + i, "detail-" + i))
                                .collect(Collectors.toList()));
    }

    @Test
    public void testCount() {
        Assert.assertEquals(10, todoService.countTodo());
    }
}
```

### Test bằng @TestConfiguration

```java
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** TodoServiceTest2 */
@RunWith(SpringRunner.class)
public class TodoServiceTest2 {

    @TestConfiguration
    public static class TodoServiceTest2Configuration {
        @Bean
        TodoService todoService() {
            return new TodoService();
        }
    }

    @MockBean TodoRepository todoRepository;

    @Autowired private TodoService todoService;

    @Before
    public void setUp() {
        Mockito.when(todoRepository.findAll())
                .thenReturn(
                        IntStream.range(0, 10)
                                .mapToObj(i -> new Todo(i, "title-" + i, "detail-" + i))
                                .collect(Collectors.toList()));
    }

    @Test
    public void testCount() {
        Assert.assertEquals(10, todoService.countTodo());
    }
}
```

## Vấn đề Test + Spring Boot 2

### @WebMvcTest

Test controller:

```java
@RunWith(SpringRunner.class)
// Bạn cần cung cấp lớp Controller cho @WebMvcTest
@WebMvcTest(TodoRestController.class)
public class TodoRestControllerTest {
    /**
     * Đối tượng MockMvc do Spring cung cấp
     * Có tác dụng giả lập request, thay thế việc khởi động Server
     */
    @Autowired
    private MockMvc mvc;
}
```

### Demo2

#### Tạo Controller

_TodoRestController.java_

```java
@RestController
@RequestMapping("/api/v1")
public class TodoRestController {
    @Autowired
    TodoService todoService;

    @GetMapping("/todo")
    public List<Todo> findAll(){
        return todoService.getAll();
    }
}

```

#### Tạo Test Controller

```java

```
