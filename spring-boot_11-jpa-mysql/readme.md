# Content

<!--toc:start-->

- [Content](#content)
  - [Spring Boot JPA](#spring-boot-jpa)
  - [Tạo Table và dữ liệu](#tạo-table-và-dữ-liệu)
  - [JpaRepository](#jparepository)
  - [DEMO](#demo)
  <!--toc:end-->

## Spring Boot JPA

**Spring Boot JPA** là một phần trong hệ sinh thái Spring Data, nó tạo ra một layer giữa tầng service và database, giúp thao tác với database dễ dàng hơn. **Spring Boot JPA** đã wrapper Hibernate và tạo ra một interface.

## Tạo Table và dữ liệu

Tạo DATABASE `micro_db`. Chứa một TABLE `user`, và script tự động insert vào db 100 `User`.

```sql
CREATE DATABASE IF NOT EXISTS micro_db;

USE micro_db;

CREATE TABLE IF NOT EXISTS user (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hp` int NULL DEFAULT NULL,
  `stamina` int DEFAULT NULL,
  `atk` int DEFAULT NULL,
  `def` int DEFAULT NULL,
  `agi` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DELIMITER $$
CREATE PROCEDURE generate_data()
BEGIN
  DECLARE i INT DEFAULT 0;
  WHILE i < 100 DO
    INSERT INTO `user` (`hp`,`stamina`,`atk`,`def`,`agi`) VALUES (i,i,i,i,i);
    SET i = i + 1;
  END WHILE;
END$$
DELIMITER ;

CALL generate_data();
```

## JpaRepository

Để sử dụng Jpa cần sử dụng interface `JpaRepository`. Yêu cầu của interface này cần cung cấp 2 thông tin:

1. Entity (Đối tượng tương tự với Table trong DB).
2. Kiểu dữ liệu của khóa chính (primary key).

Ví dụ: lấy thông tin của bảng `User`:

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
```

`@Repository` là annotation đánh dấu `UserRepository` là một Bean và chịu trách nghiệm với DB.
**Spring Boot** sẽ tự tìm thấy và khởi tạo ra đối tượng `UserRepository` trong Context. Việc tạo ra `UserRepository` hoàn toàn tự động và config.

Việc lấy ra toàn bộ `User`:

```java
@Autowired
UserRepository userRepository;

userRepository.findAll().forEach(System.out::println);
```

## DEMO

_application.properties_

```java
spring.datasource.url=jdbc:mysql://localhost:3306/micro_db?useSSL=false
spring.datasource.username=root
spring.datasource.password=root


## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8InnoDBDialect
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update

logging.level.org.hibernate = ERROR
```

_User.java_

```java
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

/** User */
@Entity
@Data
@Table(name = "user")
public class User {
    private static long serialVersionUID = -297553281792804396L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hp")
    private int hp;

    @Column(name = "stamina")
    private int stamina;

    private int atk;
    private int def;
    private int agi;
}
```

_App.java_

```java
import lombok.RequiredArgsConstructor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@RequiredArgsConstructor
public class App {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        UserRepository userRepository = context.getBean(UserRepository.class);

        // Lấy ra toàn bộ user trong db
        userRepository.findAll().forEach(System.out::println);

        // Lưu user xuống database
        User user = userRepository.save(new User());
        // Khi lưu xong, nó trả về User đã lưu kèm theo Id.
        System.out.println("User vừa lưu có ID: " + user.getId());
        Long userId = user.getId();
        // Cập nhật user.
        user.setAgi(100);
        // Update user
        // Lưu ý, lúc này đối tượng user đã có Id.
        // Nên nó sẽ update vào đối tượng có Id này
        // chứ không insert một bản ghi mới
        userRepository.save(user);

        // Query lấy ra user vừa xong để kiểm tra xem.
        User user2 = userRepository.findById(userId).get();
        System.out.println("User: " + user);
        System.out.println("User2: " + user2);

        // Xóa User khỏi DB
        userRepository.delete(user);

        // In ra kiểm tra xem userId còn tồn tại trong DB không
        User user3 = userRepository.findById(userId).orElse(null);
        System.out.println("User3: " + user3);
    }
}
```

Output chương trình:

```text
User(id=1, hp=0, stamina=0, atk=0, def=0, agi=0)
User(id=2, hp=1, stamina=1, atk=1, def=1, agi=1)
User(id=3, hp=2, stamina=2, atk=2, def=2, agi=2)
User(id=4, hp=3, stamina=3, atk=3, def=3, agi=3)
User(id=5, hp=4, stamina=4, atk=4, def=4, agi=4)
User(id=6, hp=5, stamina=5, atk=5, def=5, agi=5)
User(id=7, hp=6, stamina=6, atk=6, def=6, agi=6)
User(id=8, hp=7, stamina=7, atk=7, def=7, agi=7)
User(id=9, hp=8, stamina=8, atk=8, def=8, agi=8)
User(id=10, hp=9, stamina=9, atk=9, def=9, agi=9)
...
User vừa lưu có ID: 106
User: User(id=106, hp=0, stamina=0, atk=0, def=0, agi=100)
User2: User(id=106, hp=0, stamina=0, atk=0, def=0, agi=100)
User3: null
```
