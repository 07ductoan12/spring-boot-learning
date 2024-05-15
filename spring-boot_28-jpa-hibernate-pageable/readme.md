# Content

## Tạo Model và Repository

_User.java_

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Builder;
import lombok.Data;

/** User */
@Entity
@Data
@Builder
public class User {
    @Id @GeneratedValue private Long id;

    private String name;
}
```

_UserRepository.java_

```java
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** UserRepository */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByNameLike(String name, Pageable pageable);
}
```

_DatasourceConfig.java_

```java
import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** DatasourceConfig */
@Configuration
@RequiredArgsConstructor
public class DatasourceConfig {
    private final UserRepository userRepository;

    @PostConstruct
    public void initData() {
        userRepository.saveAll(
                IntStream.range(0, 100)
                        .mapToObj(i -> User.builder().name("name-" + i).build())
                        .collect(Collectors.toList()));
    }
}
```

## Pageable

Để có thể query lấy dữ liệu theo dạng Page, **Spring Data JPA** hỗ trợ bằng đối tượng `Pageable`
Hàm `findAll(Pageable, pageable)` là có sẵn trả về đối tượng Page<T>

```java
// Lấy ra 5 user đầu tiên
// PageRequest.of(0,5) tương đương với lấy ra page đầu tiên, và mỗi page sẽ có 5 phần tử
// PageRequest là một đối tượng kế thừa Pageable
Page<User> page = userRepository.findAll(PageRequest.of(0, 5));
```

để lấy ra 5 user tiếp theo có 2 cách

```java
// tận dụng đối tượng Page trước đó
page.nextPageable()

// Sử dụng PageRequest mới
PageRequest.of(1, 5)
```

## Sorting

Có thể query theo dạng `Page` kèm theo yêu cầu sorting theo một môi trường nào đó

```java
Page<User> page = userRepository.findAll(PageRequest.of(1, 5, Sort.by("name").descending()));
```

## Demo

_App.java_

```java

```
