package ru.litvitnik.testtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.litvitnik.testtask.entities.User;
import ru.litvitnik.testtask.exceptions.IncorrectNameException;
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
        if(searchQuery.isPresent()){
            if(searchQuery.get().length() > 100) throw new IncorrectNameException();
            return userService.getUsersMatching(searchQuery.get());
        }
        return userService.getUsers();
    }
    @GetMapping("users/{id}")
    public User getUserById(@PathVariable("id") String id){
       return userService.getUser(id);
    }

    @PostMapping("users")
    public ResponseEntity<String> addUser(@RequestParam String name){
        if(name.length() > 100 || name.length() < 1) throw new IncorrectNameException();
        String resultId = userService.addUser(new User(name));
        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resultId)
                .toUriString();
        location = location.substring(location.indexOf("/users"));
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location.split("\\?")[0]).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable("id") String id){
        userService.deleteUser(id);
    }

    @PutMapping("users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editUser(@PathVariable("id") String id, @RequestParam String newName){
        if(newName.length() > 100) throw new IncorrectNameException();
        userService.editUser(id, newName);
    }

}
