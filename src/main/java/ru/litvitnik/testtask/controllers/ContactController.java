package ru.litvitnik.testtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.exceptions.IncorrectNameException;
import ru.litvitnik.testtask.exceptions.IncorrectPhoneNumberException;
import ru.litvitnik.testtask.services.UserService;
import java.util.List;
import java.util.Optional;

@RestController
public class ContactController {
    String phoneRegex = "\\(?\\+[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})? ?(\\w{1,10}\\s?\\d{1,6})?\n";

    public UserService userService;
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @GetMapping("users/{userId}/contacts")
    public List<Contact> getContacts(@PathVariable String userId, @RequestParam Optional<String> searchQuery){
        if(searchQuery.isPresent()){
            if(searchQuery.get().length() > 100) throw new IncorrectNameException();
            return userService.getContactByNumber(userId, searchQuery.get());
        }
        return userService.getUserContactList(userId);
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
    public ResponseEntity<Void> addContact(@PathVariable String userId,
                                          @RequestParam String name,
                                          @RequestParam String number){
        if(name.length() > 100) throw new IncorrectNameException();
        if(!number.matches(phoneRegex))
            throw new IncorrectPhoneNumberException();
        String resultId = userService.addContact(userId, new Contact(name, number));
        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resultId)
                .toUriString();
        location = location.substring(location.indexOf("/contacts"));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.split("\\?")[0])
                .build();

    }
    @PutMapping("users/{userId}/contacts/{contactId}")
    public void editContact(@PathVariable String userId, @PathVariable String contactId, @RequestParam Optional<String> newName, @RequestParam Optional<String> newNumber){
        if(newName.isPresent()){
            if(newName.get().length() > 100) throw new IncorrectNameException();
        }
        if(newNumber.isPresent()){
            if(!newNumber.get().matches(phoneRegex))
                throw new IncorrectPhoneNumberException();
        }
        userService.editContact(userId, contactId, newName, newNumber);
    }
}
