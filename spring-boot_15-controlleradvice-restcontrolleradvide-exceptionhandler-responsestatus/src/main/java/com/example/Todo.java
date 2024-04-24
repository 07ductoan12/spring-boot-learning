package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Todo */
@Data
@AllArgsConstructor
public class Todo {
    private String todo;
    private String details;
}
