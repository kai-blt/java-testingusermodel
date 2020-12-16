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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    public void getUserByIdNotFound() throws Exception {
        //Mock API Url
        String apiUrl = "/users/user/101";

        //Mock the service call to find user by id
        Mockito.when(userService.findUserById(101)).thenReturn(null);

        //Make a request builder to mock client request
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        //Perform the request and return the result
        MvcResult result = mockMvc.perform(rb).andReturn();

        //Get the response from the result and convert to string
        String testResult = result.getResponse().getContentAsString();

        String expectedResult = "";

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getUserByName() throws Exception{
        //Mock API Url
        String apiUrl = "/users/user/name/Testadmin1";

        //Mock the service call to find user by id
        Mockito.when(userService.findByName("Testadmin1")).thenReturn(userList.get(0));

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
    public void getUserByNameNotFound() throws Exception{
        //Mock API Url
        String apiUrl = "/users/user/name/x";

        //Mock the service call to find user by id
        Mockito.when(userService.findByName("x")).thenReturn(null);

        //Make a request builder to mock client request
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        //Perform the request and return the result
        MvcResult result = mockMvc.perform(rb).andReturn();

        //Get the response from the result and convert to string
        String testResult = result.getResponse().getContentAsString();

        String expectedResult = "";

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getUserLikeName() throws Exception{
        //Mock API Url
        String apiUrl = "/users/user/name/like/admin";

        //Mock the service call to find user by id
        Mockito.when(userService.findByNameContaining("admin")).thenReturn(userList);

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
    public void addNewUser() throws Exception {
        //Mock API Url
        String apiUrl = "/users/user";

        //Mock up a user
        User u1 = new User("Testadmin3",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(30);

        //Mock their role
        Role r1 = new Role("admin");
        r1.setRoleid(1);

        u1.getRoles()
                .add(new UserRoles(u1, r1));

        //Mock their email
        u1.getUseremails()
                .add(new Useremail(u1,"admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(11);

        //Convert from Java to JSON Object Mapper
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);

        //When save method called from service, provide return our user
        Mockito.when(userService.save(any(User.class)))
                .thenReturn(u1);

        //Mock the post request
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/users/user/{userid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl,
                "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}