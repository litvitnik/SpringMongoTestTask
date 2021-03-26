package ru.litvitnik.testtask.entities;

import org.springframework.data.annotation.Id;


public class User {
    @Id
    public String id;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
