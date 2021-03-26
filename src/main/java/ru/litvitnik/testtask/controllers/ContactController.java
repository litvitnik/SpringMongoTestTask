package ru.litvitnik.testtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.services.ContactService;
import java.util.List;
import java.util.Optional;

@RestController
public class ContactController {

    public ContactService contactService;
    @Autowired
    public void setContactService(ContactService contactService){
        this.contactService = contactService;
    }

    @GetMapping("contacts")
    public List<Contact> getContacts(@RequestParam Optional<String> searchQuery){
        if(searchQuery.isPresent()) return contactService.getContactsByNumber(searchQuery.get());
        return contactService.getContacts();
    }
    @GetMapping("contacts/{id}")
    public Contact getOneContact(@PathVariable String id){
        return contactService.getContact(id);
    }
    @DeleteMapping("contacts/{id}")
    public void deleteContact(@PathVariable String id){
        contactService.deleteContact(id);
    }
    @PostMapping("contacts")
    public void addContact(@RequestParam String name, @RequestParam String number, @RequestParam String owner){
        contactService.addContact(new Contact(name, number, owner));
    }
    @PatchMapping("contacts/{id}")
    public Contact editContact(@PathVariable("id") String id, @RequestParam Optional<String> newName, @RequestParam Optional<String> newNumber){
        synchronized (this) {
            Contact oldContact = contactService.getContact(id);
            newName.ifPresent(oldContact::setName);
            newNumber.ifPresent(oldContact::setNumber);
            return contactService.addContact(oldContact);
        }
    }
}
