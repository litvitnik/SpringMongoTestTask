package ru.litvitnik.testtask.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.litvitnik.testtask.entities.User;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Disabled
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    /*

     */
    @Test
    public void getAllUsersShouldReturnOkStatus() throws Exception {
        //this.mockMvc.perform(get("/users")).andDo(print()).andExpect(status().isOk());
        String uri = "/users";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status, "getAllUsers must return 200 status!");
        String content = mvcResult.getResponse().getContentAsString();
        User[] userList = mapFromJson(content, User[].class);
        assertTrue(userList.length > 0);
    }
    @Test
    public void postWorksFine() throws Exception {
        //this.mockMvc.perform(post("/users?name=VeryVeryTestName")).andExpect(status().is(HttpStatus.CREATED.value()));
        /*
        String uri = "/products";
        User product = new User("VeryVeryTestPerson");
        product.setId("3");
        product.setName("Ginger");

        String inputJson = super.mapToJson(product);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Product is created successfully");

         */
    }
    @Test
    public void editWorksFine() throws Exception{
        this.mockMvc.perform(post("/users?name=VeryVeryTestName")).andExpect(status().is(HttpStatus.CREATED.value()));

        this.mockMvc.perform(put("/users?name=VeryVeryTestName")).andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void getUserById() {
    }

    @Test
    void getContactsByUserId() {
    }

    @Test
    void addUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void editUser() {
    }
}