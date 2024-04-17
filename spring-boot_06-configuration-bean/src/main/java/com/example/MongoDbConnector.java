package com.example;

/** MongoDbConnector */
public class MongoDbConnector extends DatabaseConnector {

    @Override
    public void connect() {
        // TODO Auto-generated method stub
        System.out.println("Đã kết nối tới Mongodb: " + getUrl());
    }
}
