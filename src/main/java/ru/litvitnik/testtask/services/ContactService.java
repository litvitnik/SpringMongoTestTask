package ru.litvitnik.testtask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.exceptions.AlreadyPresentException;
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
    public Contact addContact(Contact newContact){
        List<Contact> contacts = contactRepository.findAllByForeignKeyUserId(newContact.getForeignKeyUserId());
        for(Contact contact : contacts){
            if(contact.getName().equals(newContact.getName())) throw new AlreadyPresentException();
            if(contact.getNumber().equals(newContact.getNumber())) throw new AlreadyPresentException();
        }
        return contactRepository.save(newContact);
    }
    public void deleteContact(String id){
        contactRepository.removeById(id);
    }
}
