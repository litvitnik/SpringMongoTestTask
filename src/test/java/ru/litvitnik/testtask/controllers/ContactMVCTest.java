package ru.litvitnik.testtask.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.litvitnik.testtask.entities.Contact;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMongoRepositories
@Transactional
public class ContactMVCTest {

    @Autowired
    private MockMvc mockMvc;


    /*
    Итоговое покрытие:
    - Получение всех контактов пользователя
        - статус код
        - возвращает корректный результат
    - Создание контакта
        - статус код
        - создает контакта корректно
        - Возвращает ошибку если имя слишком длинное
        - Возвращает ошибку если номер ненастоящий
    - Редактирование контакта
        - статус код
        - выполняет редактирование
        - возвращает ошибку если длина невалидна
        - возвращает ошибку если номер ненастоящий
        - возвращает 404 на несуществующем контакте
    - Получение одного контакта
        - статус код
        - реально возвращает пользователя
    - Поиск контакта
        - статус код
        - возвращает контакты в нужном порядке(самый первый - полное совпадение, остальные по возрастанию длины)
    - Удаление контакта
        - статус код
        - реально удаляет пользователя
     */


    /*
     String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status, "addUser should return 201 status!");
        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location, "addUser should return location of new user!");
        mockMvc.perform(get(location)).andDo(print()).andExpect(status().isOk());
     */
    @Test
    public void addContact() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        System.out.println(userLocation);
        assertNotNull(userLocation, "addUser is not working fine so im failed");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                201,
                mvcResult.getResponse().getStatus(),
                "Adding new user should result in 201 status");
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(contactLocation, "Location header is necessary");
        String newContactAddress = userLocation + contactLocation;
        mvcResult = mockMvc
                .perform(get(newContactAddress).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                200,
                mvcResult.getResponse().getStatus(),
                "Getting added contact should return 200 status code");
        ContactProjection newContact = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ContactProjection.class);
        assertEquals(
                "Granny",
                newContact.name,
                "Recieved wrong contact by given id");
        assertEquals(
                "89992416424",
                newContact.number,
                "Recieved wrong contact by given id");
    }
    @Test
    public void getContacts() throws Exception{
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location, "addUser is not working fine so im failed");
        mockMvc.perform(get(location + "/contacts")).andDo(print()).andExpect(status().isOk());
    }
    @Test
    public void getOneContact() throws Exception{

    }

    @Test
    public void editContact() throws Exception{

    }
    @Test
    public void deleteContact() throws Exception{

    }
    @Test
    public void searchContact() throws Exception{

    }
    //Suppressed because of the same reason as in UserMVCTest
    @SuppressWarnings("unused")
    static class ContactProjection{
        public String id;
        public String name;
        public String number;
        public ContactProjection(){
        }
        public ContactProjection(String id, String name, String number){
            this.id = id;
            this.name = name;
            this.number = number;
        }
    }
}
