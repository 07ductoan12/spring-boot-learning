package com.example;

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
