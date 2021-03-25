package ru.litvitnik.testtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.entities.User;
import ru.litvitnik.testtask.services.UserService;

import java.util.List;

@RestController
public class MainController {
    public UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @GetMapping("users")
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("users/{id}")
    public User getUserById(@PathVariable("id") Long id){
        return userService.getUser(id);
    }
    @GetMapping("users/{id}/contacts")
    public List<Contact> getContactsByUserId(@PathVariable("id") long id){
        return userService.getContactsByUserId(id);
    }
    @PostMapping("users")
    public void addUser(@RequestParam String name){
        userService.addUser(new User(name));
    }
    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable("id") long id){
        userService.deleteUser(id);
    }
    @PatchMapping("users/{id}")
    public void editUser(@PathVariable("id") long id, @RequestParam String name){

    }
}
