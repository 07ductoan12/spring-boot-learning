# Content

<!--toc:start-->

- [Content](#content)
  - [Cách @Autowired vận hành](#cách-autowired-vận-hành)
  - [Vấn đề của @Autowired](#vấn-đề-của-autowired)
  - [@Primary](#primary)
  - [@Qualifier](#qualifier)
  <!--toc:end-->

## Cách @Autowired vận hành

`@Autowired` đánh dấu cho Spring biết rằng sẽ tự động inject bean tương ứng vào vị trí được đánh dấu.

```java
@Component
public class Girl {
    // Đánh dấu để Spring inject một đối tượng Outfit vào đây
    @Autowired
    Outfit outfit;

    public Girl(Outfit outfit) {
        this.outfit = outfit;
    }

    // GET
    // SET
}
```

Tuy nhiên, quá trình `@Autowired` yêu cầu Class đó phải có `Constructor` hoặc `Setter` cho thuộc tính cần inject.
Như ví dụ trên Class Girl có một `Constructor` là `public Girl(Outfit outfit)` để **Spring** có thể truyền giá trị `Outfit` vào bên trong `Girl`.
Nếu bỏ `Constructor` này đi, ta cần thay thế bằng một hàm `Setter` cho Girl.

```java
@Component
public class Girl(){
    @Autowired
    Outfit outfit;

    public Girl(){}

    public void setOutfit(Outfit outfit){
        this.outfit = outfit;
    }

}
```

Cũng có thể gắn `@Autowired` lên method thay vì thuộc tính. Nó vẫn hoạt động tương tự, tự tìm Bean phù hợp với method đó và truyền vào.

```java
@Component
public class Girl {
    Outfit outfit;

    public Girl(){}

    @Autowired
    public void setOutfit(Outfit outfit) {
        this.outfit = outfit;
    }
}
```

## Vấn đề của @Autowired

Trong thực tế, với `@Autowired` khi **Spring Boot** có chứa 2 Bean cùng loại với Context. Lúc này **Spring** sẽ không biết sử dụng Bean nào để inject vào đối tượng.

Ví dụ: Class Outfit có 2 kế thừa là `Bikini` và `Naked`.

```java
import org.springframework.stereotype.Component;

public interface Outfit {
    public void wear();
}

@Component
public class Bikini implements Outfit {
    @Override
    public void wear(){
        System.out.println("Mặc bikini");
    }
}

@Component
public class Naked implements Outfit {
    @Override
    public void wear() {
        System.out.println("Đang không mặc gì");
    }
}
```

Class `Girl` yêu cầu inject một `Outfit` vào:

```java
@Component
public class Girl {
    @Autowired
    Outfit outfit;

    public Girl(Outfit outfit){
        this.outfit = outfit;
    }
}
```

Output:

```text
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in com.example.primaryqualifier.Girl required a single bean, but 2 were found:
        - bikini: defined in URL [jar:nested:/home/toan/d/learn/java/springboot/spring-boot-learning/spring-boot_02-primary-qualifier/target/spring-boot_02-primary-qualifier-0.0.1-SNAPSHOT.jar/!BOOT-INF/classes/!/com/example/primaryqualifier/Bikini.class]
        - naked: defined in URL [jar:nested:/home/toan/d/learn/java/springboot/spring-boot-learning/spring-boot_02-primary-qualifier/target/spring-boot_02-primary-qualifier-0.0.1-SNAPSHOT.jar/!BOOT-INF/classes/!/com/example/primaryqualifier/Naked.class]
```

Trong quá trình cài đặt **Spring** tìm thấy 2 đối tượng thỏa mãn cho `Outfit`. Giờ **Spring** không biết sử dụng cái nào để inject vào trong `Girl`.

## @Primary

cách giải quyết thứ nhất là sử dụng Annotation `@Primary`.

`@Primary` là annotation đánh dấu trên Bean, giúp nó luôn đc ưu tiên lựa chọn trong trường hợp có nhiều Bean thỏa mãn.

Trong ví dụ trên nếu để `Naked` là primary. Chương trình hoạt động bình thường (`Outfit` bên trong `Girl` là `Naked`).

```java
@Component
@Primary

public class Naked implements Outfit {
    @Override
    public void wear(){
        System.out.println("Đang không mặc gì");
    }
}
```

Output:

```text
Girl Instance: com.example.primaryqualifier.Girl@7d44a19
Girl Outfit: com.example.primaryqualifier.Naked@1fb2d5e
Đang không mặc gì
```

**Spring Boot** đã ưu tiên `Naked` và inject nó vào `Girl`.

## @Qualifier

Cách thứ hai, sử dụng Annotation `@Qualifier`
`@Qualifier` xác định tên của một Bean mà muốn chỉ định inject.

```java
@Component("bikini")
public class Bikini implements Outfit {
    @Override
    public void wear(){
        System.out.println("Mặc bikini");
    }
}

@Component("naked")
public class Naked implements Outfit {
    @Override
    public void wear() {
        System.out.println("Đang không mặc gì");
    }
}

@Component
public class Girl {

    Outfit outfit;

    // Đánh dấu để Spring inject một đối tượng Outfit vào đây
    @Autowired
    public Girl(@Qualifier("naked") Outfit outfit) {
        // Spring sẽ inject outfit thông qua Constructor đầu tiên
        // Ngoài ra, nó sẽ tìm Bean có @Qualifier("naked") trong context để ịnject
        this.outfit = outfit;
    }
}
```
