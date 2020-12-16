package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UserModelApplication.class,
        properties = {"command.line.runner.enabled=false"})
@AutoConfigureMockMvc
public class UserControllerTestUnitNoDB {

    //Autowire Application context
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    //Mock services
    @MockBean
    private UserService userService;

    //Mock List of Users in Database
    List<User> userList = new ArrayList<>();


    @Before
    public void setUp() throws Exception {
        //Setup Mock Seed Data
        //Mock Role
        Role r1 = new Role("admin");
        r1.setRoleid(1);

        //Mock up a User 1
        User u1 = new User("Testadmin1",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(10);

        u1.getRoles()
                .add(new UserRoles(u1, r1));

        u1.getUseremails()
                .add(new Useremail(u1,"admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(11);

        //Add user to our mock database
        userList.add(u1);


        //Mock up a User 2
        User u2 = new User("TestUser2",
                "test2",
                "test2@lambdaschool.local");
        u2.setUserid(20);


        u2.getRoles()
                .add(new UserRoles(u2, r1));


        u2.getUseremails()
                .add(new Useremail(u2,"test2@email.local"));
        u2.getUseremails().get(0).setUseremailid(21);

        //Add user to our mock database
        userList.add(u2);


        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllUsers() throws Exception{
        //Mock API Url
        String apiUrl = "/users/users";

        //Mock the service call to find all users
        Mockito.when(userService.findAll()).thenReturn(userList);

        //Make a request builder to mock client request
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        //Perform the request and return the result
        MvcResult result = mockMvc.perform(rb).andReturn();

        //Get the response from the result and convert to string
        String testResult = result.getResponse().getContentAsString();

        //Convert Array from Java to JSON Object Mapper
        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(userList);

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getUserById() throws Exception {
        //Mock API Url
        String apiUrl = "/users/user/10";

        //Mock the service call to find user by id
        Mockito.when(userService.findUserById(10)).thenReturn(userList.get(0));

        //Make a request builder to mock client request
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        //Perform the request and return the result
        MvcResult result = mockMvc.perform(rb).andReturn();

        //Get the response from the result and convert to string
        String testResult = result.getResponse().getContentAsString();

        //Convert Array from Java to JSON Object Mapper
        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(userList.get(0));

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getUserByName() {
    }

    @Test
    public void getUserLikeName() {
    }

    @Test
    public void addNewUser() {
    }

    @Test
    public void updateFullUser() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUserById() {
    }
}