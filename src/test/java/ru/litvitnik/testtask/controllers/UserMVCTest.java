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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void deleteUser() throws Exception{
        String postUri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(postUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location, "post is not working properly so im failed");
        mvcResult = mockMvc
                .perform(delete(location).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                204,
                mvcResult.getResponse().getStatus(),
                "Deleting should return 204 status code");
        mvcResult = mockMvc
                .perform(get(location).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                404,
                mvcResult.getResponse().getStatus(),
                "User is not deleted because i still can access it");
    }
    @Test
    public void getOneUser() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String postUri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(postUri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location, "post is not working properly so im failed");
        mvcResult = mockMvc
                .perform(get(location).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                200,
                mvcResult.getResponse().getStatus(),
                "Status code should be 200");
        UserProjection receivedUser = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserProjection.class);
        assertEquals(
                receivedUser.name,
                "VeryVeryTestUser",
                "Looks like you've returned wrong user");
    }

    @Test
    public void getAllUsers() throws Exception{
        String postUri = "/users?name=VeryVeryTestUser";
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(postUri).contentType(MediaType.APPLICATION_JSON_VALUE));
        MvcResult mvcResult = mockMvc
                .perform(get("/users").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status, "getAllUsers must return 200 status!");
        List<UserProjection> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
        });
        assertTrue(actual.size() > 0);
    }
    @Test
    public void searchUserByNameOrItsPart() throws Exception{
        String postUri = "/users?name=VeryVeryTestUser";
        String postUri2 = "/users?name=VeryVeryTestUserButLonger";
        String searchQuery = "/users?searchQuery=VeryVeryTestUser";
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post(postUri).contentType(MediaType.APPLICATION_JSON_VALUE));
        mockMvc.perform(post(postUri2).contentType(MediaType.APPLICATION_JSON_VALUE));
        MvcResult mvcResult = mockMvc
                .perform(get(searchQuery).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                200,
                mvcResult.getResponse().getStatus(),
                "Successful search should return OK status");
        List<UserProjection> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertEquals(actual.get(0).name, "VeryVeryTestUser", "results must be sorted by relevance");
    }
    @Test
    public void postUser() throws Exception {
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status, "addUser should return 201 status!");
        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location, "addUser should return location of new user!");
        mockMvc.perform(get(location)).andDo(print()).andExpect(status().isOk());
    }
    @Test
    public void editUser() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "/users?name=VeryVeryTestUser";
        String weirdNameChange = "LongEnoughLongEnoughLongEnoughLongEnoughLongEnough"
                + "LongEnoughLongEnoughLongEnoughLongEnoughLongEnoughLongEnough";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        assertNotNull(location, "remember that post should return location of new entity");
        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        mvcResult = mockMvc
                .perform(put(location + "?newName=VeryNewTestEntity")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                204,
                mvcResult.getResponse().getStatus(),
                "Successful edit should return 204 NO_CONTENT");
        mvcResult = mockMvc
                .perform(get(location)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        UserProjection newUser = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserProjection.class);
        assertEquals(
                newUser.name,
                "VeryNewTestEntity",
                "Edit is not successful or get method is not working fine. Check both");
        mvcResult = mockMvc
                .perform(put(location + "?newName=" + weirdNameChange)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(
                422,
                mvcResult.getResponse().getStatus(),
                "Editing with new name length over limit should result in 422 status code");

    }
    @Test
    public void editingNonExistingUserShouldReturn400() throws Exception{
        String uri = "/users/impossibleID?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(
                400,
                mvcResult.getResponse().getStatus(),
                "Attempt of editing something that is not present should result in 400 status code");
    }
    @Test
    public void getUserShouldReturn404WhenUserNotExists() throws Exception{
       int status = mockMvc.perform(MockMvcRequestBuilders.get("/users/-1")
               .accept(MediaType.APPLICATION_JSON_VALUE))
               .andReturn()
               .getResponse()
               .getStatus();
       assertEquals(
               404,
               status,
               "Accessing something that is not present should result in 404 status code");
    }
    @Test
    public void incorrectLengthShouldReturn422() throws Exception{
        //exactly 110 symbols long
        String weirdName = "BugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOO" +
                "BugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOOBugaVugaOO";
        int status = mockMvc
                .perform(post("/users?name=" + weirdName).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getStatus();
        assertEquals(422, status, "length over 100 is prohibited");
    }
/*
Предупреждения заглушены по той причине, что IDEA не видит десериализацию JSON в проекцию и не воспринимает
это как использование конструкторов.
Тем не менее удаление любого из этих конструкторов приведет к ошибке десериализации.
Использование проекции обусловлено тем, что получение информации о пользователе из базы не возвращает
список контактов потому что получение списка пользователей не должно возвращать список их контактов так же
как и получения списка друзей ВК не должно загружать их записи или тем более личные сообщения
А более того - я не готов по соображениям безопасности делать любые из этих полей изменяемыми напрямую
 */
    @SuppressWarnings("unused")
    static class UserProjection{
        public String id;
        public String name;
        public UserProjection(String id, String name){
            this.id = id;
            this.name = name;
        }
        public UserProjection(){}
    }
}
