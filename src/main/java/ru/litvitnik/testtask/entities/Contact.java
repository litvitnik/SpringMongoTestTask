package ru.litvitnik.testtask.entities;

import org.springframework.data.annotation.Id;

public class Contact {
    @Id
    private String id;

    private String name;
    private String number;
    private final String foreignKeyUserId;

    public Contact(String name, String number, String foreignKeyUserId){
        this.name = name;
        this.number = number;
        this.foreignKeyUserId = foreignKeyUserId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getForeignKeyUserId() {
        return foreignKeyUserId;
    }


}
