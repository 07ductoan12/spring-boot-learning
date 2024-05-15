package com.example;

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
