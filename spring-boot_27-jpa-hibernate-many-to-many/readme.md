# Content

<!--toc:start-->

- [Content](#content)
  - [Tạo Table](#tạo-table)
  - [Thêm dữ liệu](#thêm-dữ-liệu)
  <!--toc:end-->

## Tạo Table

_Address.java_

```java
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

/** Address */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id // đánh dấu là primary key
    @GeneratedValue // giúp tự động tăng id
    private Long id;

    private String city;
    private String province;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // Quan hệ n-n với đối tượng ở dưới (Person) (1 địa điểm có nhiều người ở)
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Không sử dụng trong toString()
    @JoinTable(
            name = "address_person", // Tạo ra một join Table tên là "address_person"
            joinColumns =
                    @JoinColumn(
                            name = "address_id"), // Trong đó, khóa ngoại chính là address_id trỏ
            // tới class hiện tại (Address)
            inverseJoinColumns =
                    @JoinColumn(name = "person_id")) // Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới
    // (Person)
    private Collection<Person> persons;
}
```

_Person.java_

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

import java.util.Collection;

/** Person */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    @Id @GeneratedValue private Long id;

    private String name;

    // mappedBy trỏ tới tên biến persons ở trong Address.
    @ManyToMany(mappedBy = "persons")
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    @EqualsAndHashCode.Exclude
    @Exclude
    private Collection<Address> addresses;
}
```

Chạy file

```java
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class App {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
```

## Thêm dữ liệu

_AddressRepository.java_

```java
import org.springframework.data.jpa.repository.JpaRepository;

/** AddressRepository */
public interface AddressRepository extends JpaRepository<Address, Long> {}
```

_PersonRepository.java_

```java
import org.springframework.data.jpa.repository.JpaRepository;

/** PersonRepository */
public interface PersonRepository extends JpaRepository<Person, Long> {}
```

chạy ứng dụng
_App.java_

```java
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class App implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Tạo đối tượng Address
        Address hanoi = Address.builder().city("Hanoi").build();
        Address hanoi2 = Address.builder().city("Hanoi2").build();

        // Tạo đối tượng Person
        Person person1 = Person.builder().name("toan1").build();
        Person person2 = Person.builder().name("toan2").build();

        // set Persons vào address
        List<Person> listHanoi = new ArrayList<>();
        listHanoi.add(person1);
        listHanoi.add(person2);
        hanoi.setPersons(listHanoi);

        List<Person> listHanoi2 = new ArrayList<>();
        listHanoi2.add(person1);

        hanoi2.setPersons(listHanoi2);

        // Lưu vào db
        // Chúng ta chỉ cần lưu address, vì cascade = CascadeType.ALL nên nó sẽ lưu luôn Person.
        addressRepository.saveAndFlush(hanoi);
        addressRepository.saveAndFlush(hanoi2);

        // Vào: http://localhost:8080/h2-console/ để xem dữ liệu đã insert
        Address queryResult = addressRepository.findById(1L).get();
        System.out.println(queryResult.getCity());
        System.out.println(queryResult.getPersons());
    }
}
```

Lưu ý ở đây sử dụng `@Transactional`. Khiến toàn bộ code chạy trong hàm đều nằm trong `Session` quản lý của `Hibernate`.

Nếu không có `@Transactional` thì việc gọi `address.getPersons()` sẽ bị lỗi, vì nó không thể query xuống database để lấy dữ liệu person lên được.
