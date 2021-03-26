package ru.litvitnik.testtask.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.litvitnik.testtask.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAll();
    Optional<User> findById(String id);
    void removeById(String id);
    List<User> findAllByNameContains(String name);
}
