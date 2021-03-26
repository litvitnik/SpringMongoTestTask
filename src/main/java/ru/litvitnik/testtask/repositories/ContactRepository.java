package ru.litvitnik.testtask.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.litvitnik.testtask.entities.Contact;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String>{
    Optional<Contact> findById(String id);
    List<Contact> findAll();
    void removeById(String id);
    List<Contact> findAllByForeignKeyUserId(String id);
    List<Contact> findAllByNumber(String number);
    List<Contact> findByNumberAndForeignKeyUserId(String number, String userId);
}
