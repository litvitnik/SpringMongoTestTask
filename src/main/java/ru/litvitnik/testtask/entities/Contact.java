package ru.litvitnik.testtask.entities;


import java.util.UUID;

public class Contact {

    private String id;
    private String name;
    private String number;

    public Contact(String name, String number){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.number = number;
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



}
