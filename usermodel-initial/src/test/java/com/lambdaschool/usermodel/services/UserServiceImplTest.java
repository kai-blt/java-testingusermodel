package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = UserModelApplication.class,
    properties = {"command.line.runner.enabled=false"})
public class UserServiceImplTest {

    //Mock Up Services and Repos
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userrepos;

    @MockBean
    private RoleService roleService;

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

        //Initialize Mockito for tests
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findUserById() {
       Mockito.when(userrepos.findById(4L))
                .thenReturn(Optional.of(userList.get(0)));

       //Test that the name of the user with id 4 is testadmin
       assertEquals("testadmin1", userService.findUserById(4).getUsername());
    }

    @Test
    public void findByNameContaining() {
        Mockito.when(userrepos.findByUsernameContainingIgnoreCase("adm"))
                .thenReturn(userList);
        //Test that there is 1 result for all users whose name includes adm
        assertEquals(2, userService.findByNameContaining("adm").size());
    }

    @Test
    public void findAll() {
        Mockito.when(userrepos.findAll())
                .thenReturn(userList);
        //Test that we find 2 total users in the database
        assertEquals(2, userService.findAll().size());
    }

    @Test
    public void delete() {

    }

    @Test
    public void findByName() {
        //Test that the id of user whose name is testadmin is 4
        assertEquals(4, userService.findByName("testadmin").getUserid());
    }

    @Test
    public void save() {
        //Create Role
        Role testRole = new Role("Testadmin");
        testRole.setRoleid(1);

        //Create User to Save
        User testUser = new User("TestUser", "TestPassword", "TestEmail@email.com");
        testUser.setUserid(100);

        //Add role
        testUser.getRoles().add(new UserRoles(testUser, testRole));

        //Save user
        User savedUser = userService.save(testUser);

        //Make sure the user wasn't empty
        assertNotNull(testUser);

        //Make sure the added record's name matches what we provided
        assertEquals("Testadmin", savedUser.getUsername());
    }

    @Test
    public void update() {
    }

    @Test
    public void deleteAll() {
    }
}