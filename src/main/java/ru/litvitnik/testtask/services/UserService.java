package ru.litvitnik.testtask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.entities.User;
import ru.litvitnik.testtask.repositories.UserRepository;

import java.util.List;

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
    public User getUser(long id){
        return userRepository.getUserById(id);
    }
    public void addUser(User user){
        userRepository.addUser(user);
    }
    public void deleteUser(long id){
        userRepository.deleteUser(id);
    }
    public List<Contact> getContactsByUserId(long id){
        return userRepository.getContactsByUserId(id);
    }
}
