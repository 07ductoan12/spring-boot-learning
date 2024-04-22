# Content

## Query Creation

Trong **Spring JPA**, có một cơ chế giúp tạo ra các câu Query mà không cần viết thêm code.
Ví dụ: đối tượng `User`

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
@Table(name = "user")
@Data
public class User {
    private static final long serialVersionUID = -297553281792804396L;

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

Khi đặt tên method là: `findByAtk(int atk)`
Thì **Spring JPA** sẽ tự định nghĩa câu Query cho method này (bằng cách xử lý tên method -> có thể truy vấn dữ liệu với 1 dòng code).

## Quy tắc đặt tên method trong Spring JPA

Trong **Spring JPA**, cơ chế xây dựng truy vấn thông qua tên của method được quy định bởi:
`find...By`, `read...By`, `query...By`, `count...By` và `get...By`
phần còn lại được parse theo tên của thuộc tính (viết hoa chữ cái đầu). Nếu có điều kiện, thì các thuộc tính có thể kết hợp với nhau bằng chữ `And` hoặc `Or`.

Ví dụ:

```java
interface PersonRepository extends JpaRepository<User, Long> {
    // Dễ
    // version rút gọn
    Person findByLastname(String lastname);
    // verson đầy đủ
    Person findPersonByLastname(String lastname);

    Person findAllByLastname(String lastname);

    // Trung bình
    List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);

    // findDistinct là tìm kiếm và loại bỏ đi các đối tượng trùng nhau
    List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
    List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

    // IgnoreCase là tìm kiếm không phân biệt hoa thường, ví dụ ở đây tìm kiếm lastname
    // lastname sẽ không phân biệt hoa thường
    List<Person> findByLastnameIgnoreCase(String lastname);

    // AllIgnoreCase là không phân biệt hoa thường cho tất cả thuộc tính
    List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

    // OrderBy là cách sắp xếp thứ tự trả về
    // Sắp xếp theo Firstname ASC
    List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
    // Sắp xếp theo Firstname Desc
    List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
}
```

các thuộc tính trong tên method phải thuộc về Class đó, nếu không sẽ sảy ra lỗi. Tuy nhiên trong một số trường hợp có thể query bằng thuộc tính con.
Ví dụ: Đối tượng `Person` có thuộc tính là `Address` và trong `Address` lại có `ZipCode`:

```java
// person.address.zipCode
List<Person> findByAddressZipCode(ZipCode zipCode);
```

## @Query

Ngoài ra **Spring JPA** còn hỗ trợ một cách nguyên thủy khác.
Với cách sử dụng `@Query`, có thể sử dụng câu truy vấn JPQL(Hibernate) hoặc raw SQL.

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Khi được gắn @Query, thì tên của method không còn tác dụng nữa
    // Đây là JPQL
    @Query("select u from User u where u.emailAddress = ?1")
    User myCustomQuery(String emailAddress);

    // Đây là Native SQL
    @Query(value = "select * from User u where u.email_address = ?1", nativeQuery = true)
    User myCustomQuery2(String emailAddress);
}
```

cách chuyền tham số gọi theo thứ tự các tham số của method: `?1`, `?2`

Hoặc có thể đặt tên cho tham số thay cho `?{number}`

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // JPQL
    @Query("SELECT u FROM User u WHERE u.status = :status and u.name = :name")
    User findUserByNamedParams(@Param("status") Integer status, @Param("name") String name);

    // Native SQL
    @Query(value = "SELECT * FROM Users u WHERE u.status = :status and u.name = :name", nativeQuery = true)
    User findUserByNamedParamsNative(@Param("status") Integer status, @Param("name") String name);
}
```

## Demo

### Tạo Database

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

### Cấu hình Spring

_application.properties_

```java
spring.datasource.url=jdbc:mysql://localhost:3306/micro_db?useSSL=false
spring.datasource.username=root
spring.datasource.password=root


## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update

logging.level.org.hibernate = ERROR
```

### Tạo Model và Repository

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
@Table(name = "user")
@Data
public class User {
    private static final long serialVersionUID = -297553281792804396L;

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

_UserRepository.java_

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/** UserRepository */
public interface UserRepository extends JpaRepository {
    List<User> findAllByAtk(int atk);

    List<User> findAllByAgiBetween(int start, int end);

    @Query("SELECT u FROM u WHERE u.def = def")
    User findUserByDefQuery(@Param("def") Integer def);

    List<User> findAllByAgiGreaterThan(int agiThreshold);
}
```

### Chạy thử

_App.java_

```java
import lombok.RequiredArgsConstructor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/** App */
@SpringBootApplication
@RequiredArgsConstructor
public class App {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        UserRepository userRepository = context.getBean(UserRepository.class);

        System.out.println("Tìm user với Agi trong khoảng (25 - 30)");
        System.out.println("findAllByAgiBetween: ");
        userRepository.findAllByAgiBetween(25, 30).forEach(System.out::println);

        System.out.println("===========================================");
        System.out.println("Tìm user với Agi trong lớn hơn 97");
        System.out.println("findAllByAgiGreaterThan: ");
        userRepository.findAllByAgiGreaterThan(97).forEach(System.out::println);

        // Tìm kiếm tất cả theo Atk = 74
        System.out.println("===========================================");
        System.out.println("Tìm user với Atk = 74");
        System.out.println("findAllByAtk: ");
        userRepository.findAllByAtk(74).forEach(System.out::println);

        System.out.println("===========================================");
        System.out.println("Tìm User với def = 49");
        System.out.println("SELECT u FROM User u WHERE u.def = :def");
        User user = userRepository.findUserByDefQuery(49);
        System.out.println("User: " + user);
    }
}
```
