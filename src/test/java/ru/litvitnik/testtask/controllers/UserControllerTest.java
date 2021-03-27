package ru.litvitnik.testtask.controllers;


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
import ru.litvitnik.testtask.entities.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMongoRepositories
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllUsersShouldReturnOkStatus() throws Exception {
        String uri = "/users";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status, "getAllUsers must return 200 status!");

    }
    @Test
    public void postWorksFine() throws Exception {
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status, "addUser should return 201 status!");
        String location = mvcResult.getResponse().getHeader("Location");
        System.out.println(location);
        assertNotNull(location, "addUser should return location of new user!");
        mockMvc.perform(get(location)).andDo(print()).andExpect(status().isOk());
    }
    @Test
    public void editWorksFine() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "/users?name=VeryVeryTestUser";
        MvcResult mvcResult = mockMvc
                .perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        mockMvc
                .perform(put(location + "?newName=VeryNewTestEntity")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        mvcResult = mockMvc
                .perform(get(location).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        UserProjection newUser = objectMapper.readValue(
                mvcResult
                .getResponse()
                .getContentAsString(), UserProjection.class);
        assertEquals(newUser.name, "VeryNewTestEntity");
    }
}
class UserProjection{
    public String id;
    public String name;
    public UserProjection(String id, String name){
        this.id = id;
        this.name = name;
    }
    public UserProjection(){}
}