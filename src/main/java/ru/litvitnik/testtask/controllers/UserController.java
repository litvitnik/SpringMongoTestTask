package ru.litvitnik.testtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.entities.User;
import ru.litvitnik.testtask.services.UserService;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    public UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @GetMapping("users")
    public List<User> getUsers(@RequestParam Optional<String> searchQuery){
        if(searchQuery.isPresent()) return userService.getUsersByName(searchQuery.get());
        return userService.getUsers();
    }
    @GetMapping("users/{id}")
    public User getUserById(@PathVariable("id") String id){
       return userService.getUser(id);
    }
    @GetMapping("users/{id}/contacts")
    public List<Contact> getContactsByUserId(@PathVariable("id") String id, @RequestParam Optional<String> searchQuery){
        if(searchQuery.isPresent()) return userService.getUsersContactByNumber(id, searchQuery.get());
        return userService.getContactsByUserId(id);
    }
    @PostMapping("users")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestParam String name){
        userService.addUser(new User(name));
    }

    @DeleteMapping("users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") String id){
        userService.deleteUser(id);
    }

    @PatchMapping("users/{id}")
    public User editUser(@PathVariable("id") String id, @RequestParam Optional<String> newName){
        synchronized (this){
            User oldUser = userService.getUser(id);
            newName.ifPresent(oldUser::setName);
            return userService.addUser(oldUser);
        }
    }



}
