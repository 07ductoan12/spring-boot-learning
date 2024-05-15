# Content

<!--toc:start-->

- [Content](#content)
  - [Tạo Table](#tạo-table)
  - [Chạy thử](#chạy-thử)
  - [Thêm dữ liệu](#thêm-dữ-liệu)
  <!--toc:end-->

## Tạo Table

_Person.java_

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Builder;
import lombok.Data;

/** Person */
@Entity // Hibernate Entity
@Data // Lombok
@Builder // Lombok
public class Person {
    @Id @GeneratedValue private Long id;
    private String name;
}
```

_Address.java_

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import lombok.Builder;
import lombok.Data;

/** Address */
@Entity
@Data
@Builder
public class Address {
    @Id @GeneratedValue private Long id;

    private String city;
    private String province;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
```

## Chạy thử

```java
import lombok.RequiredArgsConstructor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

_PersionRepository.java_

```java
import org.springframework.data.jpa.repository.JpaRepository;

/** PersonRepository */
public interface PersonRepository extends JpaRepository<Person, Long> {}
```

chạy file

```java
import lombok.RequiredArgsConstructor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @Override
    public void run(String... args) throws Exception {
        Person person = Person.builder().name("Toan").build();

        personRepository.save(person);

        Address address = Address.builder().city("Hanoi").person(person).build();

        addressRepository.save(address);
    }
}
```
