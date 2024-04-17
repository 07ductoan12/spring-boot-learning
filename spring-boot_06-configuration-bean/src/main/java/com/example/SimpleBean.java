package com.example;

/** SimpleBean */
public class SimpleBean {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SimpleBean(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "This is a simple bean, name: " + username;
    }
}
