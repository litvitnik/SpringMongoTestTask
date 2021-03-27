package ru.litvitnik.testtask.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.litvitnik.testtask.entities.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);
    List<User> findAll();
    List<User> findAllByNameContains(String name);
}
