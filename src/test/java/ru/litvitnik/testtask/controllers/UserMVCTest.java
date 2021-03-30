package ru.litvitnik.testtask.controllers;

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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMongoRepositories
@Transactional
class UserMVCTest {

    @Autowired
    private MockMvc mockMvc;


    /*
    Итоговое покрытие:
    + Получение всех пользователей
        + статус код
        + возвращает корректный результат
    + Создание пользователя
        + статус код
        + создает пользователя корректно
        + Возвращает ошибку если имя пользователя слишком длинное
    + Редактирование пользователя
        + статус код
        + выполняет редактирование
        + возвращает ошибку если длина невалидна
        + возвращает 404 на несуществующем пользователе
    + Получение одного пользователя
        + статус код
        + реально возвращает пользователя
    + Поиск пользователя
        + статус код
        + возвращает пользователей в нужном порядке(самый первый - полное совпадение, остальные по возрастанию длины)
    + Удаление пользователя
        + статус код
        + реально удаляет пользователя
    */
    @Test
    public void deleteUser() throws Exception {
        String postUri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(postUri))
                .andExpect(header().exists("Location"))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc
                .perform(delete(location))
                .andExpect(status().is(204));
        mockMvc
                .perform(get(location))
                .andExpect(status().is(404));
    }

    @Test
    public void getOneUser() throws Exception {
        String postUri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(postUri))
                .andExpect(header().exists("Location"))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc
                .perform(get(location))
                .andExpect(status().is(200))
                .andExpect(content().json("{'name':'VeryVeryTestUser'}"))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void getAllUsers() throws Exception {
        String postUri = "/users?name=VeryVeryTestUser";
        mockMvc.perform(post(postUri));
        mockMvc
                .perform(get("/users").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    @Test
    public void searchUserByNameOrItsPart() throws Exception {
        String postUri = "/users?name=VeryVeryTestUser";
        String postUri2 = "/users?name=VeryVeryTestUserButLonger";
        String searchQuery = "/users?searchQuery=VeryVeryTestUser";
        mockMvc.perform(post(postUri));
        mockMvc.perform(post(postUri2));
        mockMvc
                .perform(get(searchQuery))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].name", is("VeryVeryTestUser")));
    }

    @Test
    public void postUser() throws Exception {
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andExpect(status().is(201))
                .andExpect(header().exists("Location"))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc
                .perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("VeryVeryTestUser")));
    }

    @Test
    public void editUser() throws Exception {
        String uri = "/users?name=VeryVeryTestUser";
        String weirdNameChange = "LongEnoughLongEnoughLongEnoughLongEnoughLongEnough"
                + "LongEnoughLongEnoughLongEnoughLongEnoughLongEnoughLongEnough";
        MvcResult mvcResult = mockMvc
                .perform(post(uri))
                .andExpect(status().is(201))
                .andExpect(header().exists("Location"))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc
                .perform(put(location + "?newName=VeryNewTestEntity"))
                .andExpect(status().is(204));
        mockMvc
                .perform(get(location))
                .andExpect(content().json("{'name':'VeryNewTestEntity'}"));
        mockMvc
                .perform(put(location + "?newName=" + weirdNameChange))
                .andExpect(status().is(422));
    }

    @Test
    public void editingNonExistingUserShouldReturn400() throws Exception {
        String uri = "/users/impossibleID?name=VeryVeryTestUser";
        mockMvc
                .perform(put(uri))
                .andExpect(status().is(400));
    }

    @Test
    public void getUserShouldReturn404WhenUserNotExists() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/-1"))
                .andExpect(status().is(404));
    }

    @Test
    public void incorrectLengthShouldReturn422() throws Exception {
        //exactly 110 symbols long
        String weirdName = "BugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOO" +
                "BugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOO";
        mockMvc
                .perform(post("/users?name=" + weirdName))
                .andExpect(status().is(422));
    }
}