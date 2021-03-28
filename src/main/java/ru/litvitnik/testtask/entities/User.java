package ru.litvitnik.testtask.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


public class User {
    @Id
    private String id;
    private String name;
    @JsonIgnore
    private List<Contact> contactList;

    public List<Contact> getContactList(){
        return this.contactList;
    }
    public void setContactList(List<Contact> list){
        this.contactList = list;
    }
    public User(String name) {
        this.name = name;
        this.contactList = new ArrayList<>();
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
