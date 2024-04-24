package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;

/** ErrorMessage */
@Data
@AllArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private String message;
}
