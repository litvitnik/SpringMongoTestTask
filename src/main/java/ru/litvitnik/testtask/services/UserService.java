package ru.litvitnik.testtask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.entities.User;
import ru.litvitnik.testtask.exceptions.AlreadyPresentException;
import ru.litvitnik.testtask.exceptions.NotFoundException;
import ru.litvitnik.testtask.repositories.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    @Autowired
    public void setUsersRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public List<User> getUsersMatching(String namePart){
        return userRepository.findAllByNameContains(namePart);
    }

    public User getUser(String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new NotFoundException();
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent())userRepository.deleteById(id);
        else throw new NotFoundException();
    }

    public List<Contact> getUserContactList(String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) return optionalUser.get().getContactList();
        throw new NotFoundException();
    }

    public Contact getOneContact(String userId, String contactId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) throw new NotFoundException();
        List<Contact> contacts = optionalUser.get().getContactList();
        return contacts.stream()
                .filter(contact -> contact.getId().equals(contactId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
    public void deleteContact(String userId, String contactId){
        User user = this.getUser(userId);
        List<Contact> contacts = user.getContactList();
        if (!contacts.removeIf(contact -> contact.getId().equals(contactId))) throw new NotFoundException();
    }
    public void addContact(String userId, Contact newContact){
        User user = this.getUser(userId);
        List<Contact> contacts = user.getContactList();
        if(contacts.stream()
                .anyMatch(oldContact ->
                        oldContact.getName().equals(newContact.getName())
                        || oldContact.getNumber().equals(newContact.getNumber())
                )) throw new AlreadyPresentException();
        contacts.add(newContact);
        user.setContactList(contacts);
        userRepository.save(user);
    }
    public Contact editContact(String userId, String contactId, Optional<String> newName, Optional<String> newNumber){
        User user = this.getUser(userId);
        List<Contact> contacts = user.getContactList();
        Contact contact = this.getOneContact(userId, contactId);
        int modifyingIndex = contacts.indexOf(contact);
        newName.ifPresent(contacts.get(modifyingIndex)::setName);
        newNumber.ifPresent(contacts.get(modifyingIndex)::setNumber);
        user.setContactList(contacts);
        userRepository.save(user);
        return this.getOneContact(userId, contactId);
    }
    public List<Contact> getContactByNumber(String userId, String number){
        return List.of(this.getUserContactList(userId)
                .stream()
                .filter(contact -> contact.getNumber().equals(number))
                .findAny()
                .orElseThrow(NotFoundException::new));
    }
}
