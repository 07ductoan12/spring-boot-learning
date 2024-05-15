# Content

<!--toc:start-->

- [Content](#content)
  - [Tạo Table](#tạo-table)
  - [Thêm dữ liệu](#thêm-dữ-liệu)
  <!--toc:end-->

## Tạo Table

_Person.java_

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Person */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id @GeneratedValue private Long id;

    private String name;

    // Many to One Có nhiều người ở 1 địa điểm.
    @ManyToOne
    @JoinTable(name = "address_id") // thông qua khóa ngoại address_id
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Address address;
}
```

_Address.java_

```java
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id @GeneratedValue private Long id;

    private String city;
    private String province;

    @OneToMany(
            mappedBy = "address",
            cascade =
                    CascadeType
                            .ALL) // Quan hệ 1-n với đối tượng ở dưới (Person) (1 địa điểm có nhiều
                                  // người ở)
    // MapopedBy trỏ tới tên biến Address ở trong Person.
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoonhg sử dụng trong toString()
    private Collection<Person> persons;
}
```

_application.properties_

```java
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
# Không có password, vào thẳng luôn
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# Cho phép vào xem db thông qua web
spring.h2.console.enabled=true
```

Chạy thử file
_App.java_

```java
import lombok.RequiredArgsConstructor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** App */
@SpringBootApplication
@RequiredArgsConstructor
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
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

khởi động chương trình

```java
import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

/** App */
@SpringBootApplication
@RequiredArgsConstructor
public class App implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    // Sử dụng @RequiredArgsConstructor và final để thay cho @Autowired
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @Override
    public void run(String... args) throws Exception {
        // Tạo ra đối tượng Address có tham chiếu tới person
        Address address = new Address();
        address.setCity("Hanoi");

        // Tạo ra đối tượng person
        Person person = new Person();
        person.setName("Toan");
        person.setAddress(address);

        address.setPersons(Collections.singleton(person));

        // Chúng ta chỉ cần lưu address, vì cascade = CascadeType.ALL nên nó sẽ lưu luôn Person.
        addressRepository.saveAndFlush(address);

        personRepository
                .findAll()
                .forEach(
                        p -> {
                            System.out.println(p.getId());
                            System.out.println(p.getName());
                            System.out.println(p.getAddress());
                        });
    }
}
```

Ouput:

```
1
Toan
Address(id=1, city=Hanoi, province=null)
```
