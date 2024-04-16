# Content

## Kiến trúc trong Spring Boot

Kiến trúc MVC trong Spring Boot được xây dựng dựa trên tư tưởng "độc lập" kết hợp với lập trình hướng đối tượng. Độc lập: các layer phục vụ các mục đích nhất định, khi muốn thực hiện một công việc ngoài phạm vi thì sẽ đưa công việc xuống các layer thấp hơn.
Kiến trúc Controller - Service - Repository chia project thành 3 lớp:

- **Consumer Layer hay Controller**: là tầng giao tiếp với bên ngoài và handler các request từ bên ngoài hệ thống.
- **Service Layer**: Thực hiện các nghiệp vụ và xử lý logic.
- **Repository Layer**: Chịu trách nghiệm giao tiếp với các DB, thiết bị lưu trữ, xử lý query và trả về các kiểu dữ liệu mà tầng Service yêu cầu.

Để phục vụ cho kiến trúc ở trên, **Spring Boot** tạo ra 3 Annotation là `@Controller`, `@Service` và `@Repository` đánh dấu các tầng với nhau đánh dấu các tầng với nhau.

## Implement

model `Girl`:

```java
public class Girl {

    private String name;

    public Girl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Girl{" + this.name + "}";
    }
}
```

interface `GirlRepository`:

```java
public interface GirlRepository {

    Girl getGirlName(String name);
}
```

Triển khai `GirlRepository` và đánh dấu `@Repository`:

```java
@Repository
public class GirlRepositoryImpl implements GirlRepository {

    @Override
    public Girl getGirlName(String name) {
        // TODO Auto-generated method stub
        return new Girl(name);
    }
}
```

Class `GirlService` để giải quyết các logic nghiệp vụ. Lớp `GirlService` sẽ giao tiếp với DB thông qua `GirlRepository`.

```java
@Service
public class GirlService {
    @Autowired private GirlRepository girlRepository;

    public String randomGirlName(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }

    public Girl getRandomGirl() {
        String name = randomGirlName(10);
        return girlRepository.getGirlName(name);
    }
}
```

Chạy chương trình:

```java
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        // Lấy ra bean GirlService
        GirlService girlService = context.getBean(GirlService.class);
        // Lấu ra random một cô gái từ tầng service
        Girl girl = girlService.getRandomGirl();
        // In ra màn hình
        System.out.println(girl);

    }
}
```

Output:

```text
Girl{0yxywifd1b}
```

## Giải thích

Về bản chất `@Service` và `@Repository` cũng chính là `@Component`. Nhưng đặt tên khác nhau để phân biệt được các tầng với nhau.
`@Component` đánh dấu cho Spring Boot biết Class đó là `Bean` -> `@Service` và `@Repository` cũng vậy.
