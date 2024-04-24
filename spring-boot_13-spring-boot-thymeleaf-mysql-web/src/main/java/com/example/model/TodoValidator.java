package com.example.model;

import org.thymeleaf.util.StringUtils;

import java.util.Optional;

/** TodoValidator: kiểm tra xem một Object Todo có hợp lệ không */
public class TodoValidator {
    /**
     * Kiểm tra một object Todo có hợp lệ không
     *
     * @param todo
     * @return
     */
    public boolean isValid(Todo todo) {
        return Optional.ofNullable(todo)
                .filter(t -> StringUtils.isEmpty(t.getTitle())) // Kiểm tra title khác rỗng
                .filter(t -> StringUtils.isEmpty(t.getDetails())) // Kiểm tra detail khác rỗng
                .isPresent(); // Trả về true nếu hợp lệ, ngược lại false
    }
}
