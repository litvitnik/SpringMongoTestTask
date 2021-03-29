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
import org.springframework.transaction.annotation.Transactional;
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
    + Получение всех контактов пользователя
        + статус код
        + возвращает корректный результат
    + Создание контакта
        + статус код
        + создает контакта корректно
        + Возвращает ошибку если имя слишком длинное
        + Возвращает ошибку если номер ненастоящий
    + Редактирование контакта
        + статус код
        + выполняет редактирование
        + возвращает ошибку если длина невалидна
        + возвращает ошибку если номер ненастоящий
        + возвращает 404 на несуществующем контакте
    + Получение одного контакта
        + статус код
        + реально возвращает пользователя
    + Поиск контакта
        + статус код
        + возвращает контакты в нужном порядке(самый первый - полное совпадение, остальные по возрастанию длины)
    + Удаление контакта
        + статус код
        + реально удаляет пользователя
     */


    @Test
    public void addContact() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(userLocation, "addUser is not working fine so im failed");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                201,
                mvcResult.getResponse().getStatus(),
                "Adding new contact should result in 201 status");
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(contactLocation, "Location header is necessary");
        mvcResult = mockMvc
                .perform(get(contactLocation).contentType(MediaType.APPLICATION_JSON_VALUE))
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
                "Received wrong contact by given id");
    }
    @Test
    public void addContactToNonExistingUser() throws Exception{
        String contactUri = "/users/-1/contacts?name=WhoCaresWhatIsHere&number=89348765544";
        MvcResult mvcResult = mockMvc
                .perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                404,
                mvcResult.getResponse().getStatus(),
                "adding contact to non-existing user should return 404");
    }
    @Test
    public void addWrongName() throws Exception{
        String weirdName = "GrannyManyGrannyManyGrannyManyGrannyManyGrannyMany" +
                "GrannyManyGrannyManyGrannyManyGrannyManyGrannyManyGrannyMany";
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(userLocation, "addUser is not working fine so im failed");
        String contactUri = userLocation + "/contacts?name=" + weirdName + "&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                422,
                mvcResult.getResponse().getStatus(),
                "Adding incorrect contact name should return UNPROCESSABLE_ENTITY");
    }
    @Test
    public void addWrongNumber() throws Exception{
        String weirdNumber = "im not even a number lol";
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(userLocation, "addUser is not working fine so im failed");
        String contactUri = userLocation + "/contacts?name=Granny&number=" + weirdNumber;
        mvcResult = mockMvc
                .perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                422,
                mvcResult.getResponse().getStatus(),
                "Adding incorrect contact number should return UNPROCESSABLE_ENTITY");
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
        mvcResult = mockMvc
                .perform(get(contactLocation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                200,
                mvcResult.getResponse().getStatus(),
                "Getting added contact should return 200 status code");
    }

    @Test
    public void editContact() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "/users?name=VeryVeryTestUser";
        String weirdName = "GrannyManyGrannyManyGrannyManyGrannyManyGrannyMany" +
                "GrannyManyGrannyManyGrannyManyGrannyManyGrannyManyGrannyMany";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        System.out.println(contactLocation);
        assertNotNull(contactLocation, "post didn't work");
        String tryingToEditNonExisting = userLocation + "/contacts/-1?newName=anything";
        mvcResult = mockMvc
                .perform(put(tryingToEditNonExisting).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                404,
                mvcResult.getResponse().getStatus(),
                "editing non existing contact should return 404 status code");
        String wrongPut1 = contactLocation + "?newName=" + weirdName;
        String wrongPut2 = contactLocation + "?newNumber=ImNotEvenANumberLol";
        mvcResult = mockMvc
                .perform(put(wrongPut1).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                422,
                mvcResult.getResponse().getStatus(),
                "too long name should not be put");
        mvcResult = mockMvc
                .perform(put(wrongPut2).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                422,
                mvcResult.getResponse().getStatus(),
                "bad number name should not be put");
        String goodPut = contactLocation + "?newName=noLongerGranny";
        mvcResult = mockMvc
                .perform(put(goodPut).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                204,
                mvcResult.getResponse().getStatus(),
                "good put should return 204 status code");
        mvcResult = mockMvc
                .perform(get(contactLocation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        ContactProjection newContact = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ContactProjection.class);
        assertEquals(
                "noLongerGranny",
                newContact.name,
                "edit didn't happen");
    }
    @Test
    public void deleteContact() throws Exception{
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
                "Seems like contact is not created or status code is not returned");
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(contactLocation, "Location header is necessary to farther actions");
        mvcResult = mockMvc
                .perform(get(contactLocation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                200,
                mvcResult.getResponse().getStatus(),
                "Contact is not created so im failed");
        mvcResult = mockMvc
                .perform(delete(contactLocation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                204,
                mvcResult.getResponse().getStatus(),
                "NO_CONTENT code expected, make sure deleteMapping is working properly");
        mvcResult = mockMvc
                .perform(get(contactLocation).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                404,
                mvcResult.getResponse().getStatus(),
                "contact is still present");
    }
    @Test
    public void searchContact() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mockMvc.perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String contactUri2 = userLocation + "/contacts?name=GrannyTheSecond&number=893324566775";
        mockMvc.perform(post(contactUri2).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        mvcResult = mockMvc
                .perform(get(userLocation + "/contacts?searchQuery=89992416424").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        List<ContactProjection> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertTrue(actual.size() > 0, "search is not working at all");
        assertEquals(
                actual.get(0).name,
                "Granny",
                "full match should be the first in matching list");
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
