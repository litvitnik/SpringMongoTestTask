package ru.litvitnik.testtask.controllers;


import org.junit.jupiter.api.Disabled;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMongoRepositories
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllUsersShouldReturnOkStatus() throws Exception {
        //this.mockMvc.perform(get("/users")).andDo(print()).andExpect(status().isOk());
        String uri = "/users";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status, "getAllUsers must return 200 status!");
        String content = mvcResult.getResponse().getContentAsString();
        //assertTrue(userList.length > 0);
    }
    @Test
    public void postWorksFine() throws Exception {
        //this.mockMvc.perform(post("/users?name=VeryVeryTestName")).andExpect(status().is(HttpStatus.CREATED.value()));

        String uri = "/users?name=VeryVeryTestUser";
        //User testUser = new User("VeryVeryTestPerson");
        //String inputJson = mapToJson(testUser);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        String location = mvcResult.getResponse().getHeader("Location");
        System.out.println(location);
        //assertEquals(content, "Product is created successfully");
    }
    @Test
    @Disabled
    public void editWorksFine() throws Exception{
       // this.mockMvc.perform(post("/users?name=VeryVeryTestName")).andExpect(status().is(HttpStatus.CREATED.value()));

       // this.mockMvc.perform(put("/users?name=VeryVeryTestName")).andExpect(status().is(HttpStatus.CREATED.value()));
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