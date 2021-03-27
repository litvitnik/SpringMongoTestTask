package ru.litvitnik.testtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class ContactController {

    public UserService userService;
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @GetMapping("users/{id}/contacts")
    public List<Contact> getContacts(@PathVariable String id, @RequestParam Optional<String> searchQuery){
        return userService.getUserContactList(id);
    }
    @GetMapping("users/{userId}/contacts/{contactId}")
    public Contact getOneContact(@PathVariable String userId, @PathVariable String contactId){
        return userService.getOneContact(userId, contactId);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("users/{userId}/contacts/{contactId}")
    public void deleteContact(@PathVariable String userId, @PathVariable String contactId){
        userService.deleteContact(userId, contactId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("users/{userId}/contacts")
    public void addContact(@PathVariable String userId, @RequestParam String name, @RequestParam String number){
        userService.addContact(userId, new Contact(name, number));
    }
    @PatchMapping("users/{userId}/contacts/{contactId}")
    public Contact editContact(@PathVariable String userId, @PathVariable String contactId, @RequestParam Optional<String> newName, @RequestParam Optional<String> newNumber){
        return userService.editContact(userId, contactId, newName, newNumber);
    }
}
