package ru.litvitnik.testtask.repositories;

import org.springframework.stereotype.Service;
import ru.litvitnik.testtask.entities.Contact;
import ru.litvitnik.testtask.entities.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRepository {
    static ArrayList<User> users = new ArrayList<>();
    static {
        users.add(new User("Kate", new ArrayList<>(List.of(new Contact("Mom", "+7998887766")))));
        users.add(new User("Mike", new ArrayList<>(List.of(new Contact("Father", "+79215554433")))));
        users.add(new User("Peter", new ArrayList<>(List.of(new Contact("Son", "+79403337755")))));
    }
    public List<User> findAll(){
        return users;
    }
    public User getUserById(long id){
        return users.get((int) id);
    }
    public void addUser(User user){
        users.add(user);
    }
    public void deleteUser(long id){
        users.remove((int) id);
    }
    public List<Contact> getContactsByUserId(long id){
        return users.get((int) id).getContacts();
    }
}
