package ru.litvitnik.testtask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.exceptions.NotFoundException;
import ru.litvitnik.testtask.repositories.ContactRepository;
import ru.litvitnik.testtask.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private ContactRepository contactRepository;

    @Autowired
    public void setContactRepository(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    public List<Contact> getContacts(){
        return contactRepository.findAll();
    }
    public Contact getContact(String id){
        return contactRepository.findById(id).orElseThrow(NotFoundException::new);
    }
    public List<Contact> getContactsByNumber(String number){
        return contactRepository.findAllByNumber(number);
    }
    public void addContact(Contact contact){
        contactRepository.save(contact);
    }
    public void deleteContact(String id){
        contactRepository.removeById(id);
    }
    public void editContact(Contact newOne){
        contactRepository.save(newOne);
    }
}
