package ru.litvitnik.testtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.litvitnik.testtask.repositories.ContactRepository;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableMongoRepositories
//@EnableJpaRepositories("ru.litvitnik.testtask.repositories")
public class TesttaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesttaskApplication.class, args);
	}

}
