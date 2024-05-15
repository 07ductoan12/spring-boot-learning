package com.example;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
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
