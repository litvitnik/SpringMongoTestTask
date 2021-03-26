package ru.litvitnik.testtask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.entities.User;
import ru.litvitnik.testtask.exceptions.NotFoundException;
import ru.litvitnik.testtask.repositories.ContactRepository;
import ru.litvitnik.testtask.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private ContactRepository contactRepository;

    @Autowired
    public void setContactRepository(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }
    @Autowired
    public void setUsersRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public List<User> getUsersByName(String name){
        return userRepository.findAllByNameContains(name);
    }

    public User getUser(String id){
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.removeById(id);
    }

    public List<Contact> getContactsByUserId(String id){
        return contactRepository.findAllByForeignKeyUserId(id);
    }
    public List<Contact> getUsersContactByNumber(String id, String number){
        return contactRepository.findByNumberAndForeignKeyUserId(id, number);
    }
}
