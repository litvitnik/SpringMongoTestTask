package ru.litvitnik.testtask.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andExpect(status().is(201))
                .andExpect(header().exists("Location"))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(userLocation, "addUser is not working fine so im failed");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri))
                .andExpect(status().is(201))
                .andReturn();
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        mockMvc
                .perform(get(contactLocation))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name", is("Granny")))
                .andExpect(jsonPath("$.number", is("89992416424")));
    }
    @Test
    public void addContactToNonExistingUser() throws Exception{
        String contactUri = "/users/-1/contacts?name=WhoCaresWhatIsHere&number=89348765544";
        mockMvc
                .perform(post(contactUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));
    }
    @Test
    public void addWrongName() throws Exception{
        String weirdName = "GrannyManyGrannyManyGrannyManyGrannyManyGrannyMany" +
                "GrannyManyGrannyManyGrannyManyGrannyManyGrannyManyGrannyMany";
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        String contactUri = userLocation + "/contacts?name=" + weirdName + "&number=89992416424";
        mockMvc
                .perform(post(contactUri))
                .andExpect(status().is(422));
        mockMvc
                .perform(get(userLocation))
                .andExpect(jsonPath("$.name", is("VeryVeryTestUser")));
    }
    @Test
    public void addWrongNumber() throws Exception{
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        String wrongNumber = userLocation + "/contacts?name=Granny&number=ImNotEvenANumber";
        mockMvc
                .perform(post(wrongNumber))
                .andExpect(status().is(422));
    }
    @Test
    public void getContacts() throws Exception{
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc
                .perform(get(location + "/contacts"))
                .andExpect(status().isOk());
    }
    @Test
    public void getOneContact() throws Exception{
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri))
                .andExpect(status().is(201))
                .andReturn();
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        mockMvc
                .perform(get(contactLocation))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Granny")))
                .andExpect(jsonPath("$.number", is("89992416424")));
    }

    @Test
    public void editContact() throws Exception{
        String uri = "/users?name=VeryVeryTestUser";
        String weirdName = "GrannyManyGrannyManyGrannyManyGrannyManyGrannyMany" +
                "GrannyManyGrannyManyGrannyManyGrannyManyGrannyManyGrannyMany";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri))
                .andReturn();
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        String tryingToEditNonExisting = userLocation + "/contacts/-1?newName=anything";
        mockMvc
                .perform(put(tryingToEditNonExisting))
                .andExpect(status().is(404));
        String wrongPut1 = contactLocation + "?newName=" + weirdName;
        String wrongPut2 = contactLocation + "?newNumber=ImNotEvenANumberLol";
        mockMvc
                .perform(put(wrongPut1))
                .andExpect(status().is(422));
        mockMvc
                .perform(put(wrongPut2))
                .andExpect(status().is(422));
        mockMvc
                .perform(get(contactLocation))
                .andExpect(jsonPath("$.name", is("Granny")))
                .andExpect(jsonPath("$.number", is("89992416424")));
        String goodPut = contactLocation + "?newName=noLongerGranny";
        mockMvc
                .perform(put(goodPut))
                .andExpect(status().is(204));
        mockMvc
                .perform(get(contactLocation))
                .andExpect(jsonPath("$.name", is("noLongerGranny")));
    }
    @Test
    public void deleteContact() throws Exception{
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(userLocation, "addUser is not working fine so im failed");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mvcResult = mockMvc
                .perform(post(contactUri))
                .andExpect(status().is(201))
                .andReturn();
        String contactLocation = mvcResult.getResponse().getHeader("Location");
        assertNotNull(contactLocation, "Location header is necessary to farther actions");
        mockMvc
                .perform(get(contactLocation))
                .andExpect(status().is(200))
                .andReturn();
        mockMvc
                .perform(delete(contactLocation))
                .andExpect(status().is(204))
                .andReturn();
        mockMvc
                .perform(get(contactLocation))
                .andExpect(status().is(404));
    }
    @Test
    public void searchContact() throws Exception{
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andReturn();
        String userLocation = mvcResult.getResponse().getHeader("Location");
        String contactUri = userLocation + "/contacts?name=Granny&number=89992416424";
        mockMvc.perform(post(contactUri));
        mockMvc
                .perform(get(userLocation + "/contacts?searchQuery=89992416424"))
                .andExpect(jsonPath("$[0].name", is("Granny")))
                .andExpect(jsonPath("$[0].number", is("89992416424")));
    }
}
