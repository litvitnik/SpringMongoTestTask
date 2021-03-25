package ru.litvitnik.testtask.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static long counter = 0;
    private long id;
    private String name;
    private List<Contact> contacts;

    public User(String name, List<Contact> contacts) {
        this.id = counter++;
        this.name = name;
        this.contacts = contacts;
    }

    public User(String name) {
        this.id = counter++;
        this.name = name;
        this.contacts = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
    public void setId(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Contact> getContacts() {
        return contacts;
    }
}
