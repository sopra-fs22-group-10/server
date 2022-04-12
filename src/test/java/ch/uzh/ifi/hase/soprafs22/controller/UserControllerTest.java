package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;

import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginDTO;

import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.LoginGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {
    User user;
    UserLoginDTO userLoginDTO;


    @BeforeEach
    public void init() {
        user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setAuthentication("testAuthentication");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);
    }

    @AfterEach
    public void teardown() {
        userService.deleteUser(user);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test //get /users -> 200
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given predfined user
        List<User> allUsers = Collections.singletonList(user);
        // this mocks the UserService -> we define above what the userService should return when getUsers() is called

        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test //post /users -> 201 : successful register
    public void createUser_validInput_userCreated() throws Exception {
        // given predefined user
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(user.getPassword());

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLoginDTO));

        // then
        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andReturn();
        String authentication = mvcResult.getResponse().getHeader("Authentication");
        assertEquals(authentication, user.getAuthentication());
    }

    @Test //post /users -> 409 : register for taken username
    public void createUser_usernameTaken() throws Exception {
        // given predefined user
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(user.getPassword());
        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.resolve(409), "Username is taken!"));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)

                .content(asJsonString(userLoginDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(409));
    }

    @Test //get /users/1 -> 200 : successfully retrieve data for user with ID 1
    public void get_user_from_ID() throws Exception {
        // given predfined user
        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUserByID(user.getId())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/"+user.getId()).contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().is(200))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test //get /users/1 -> 404 : fail at retrieving data because user 1 doesnt exist
    public void get_user_from_wrong_ID() throws Exception {
        // given predfined user
        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUserByID(user.getId())).willThrow(new ResponseStatusException(HttpStatus.resolve(404),
                "No account for this userID was found!"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/"+user.getId()).contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().is(404));
    }

    @Test //put /users/1 -> 204 : successfully update data for user with ID 1
    public void update_user_with_ID() throws Exception {
        // given predfined user
        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUserByID(user.getId())).willReturn(user);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(user.getUsername());

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().is(204));
    }

    @Test //put /users/1 -> 404 : fail at updating data because user 1 doesnt exist
    public void update_user_with_wrong_ID() throws Exception {
        // given predfined user
        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUserByID(user.getId())).willThrow(new ResponseStatusException(HttpStatus.resolve(404),
                "No account for this userID was found!"));

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(user.getUsername());

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().is(404));
    }

    @Test //put /users/1 -> 401 : fail at updating data because authentification is incorrect
    public void update_user_with_wrong_auth() throws Exception {
        // given predfined user
        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUserByID(user.getId())).willReturn(user);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(user.getUsername());

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", "wrongAuth")
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().is(401));
    }

    @Test //post /loginrequests -> 200 : successful login
    public void user_login_success() throws Exception {
        // given predefined user
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(user.getPassword());

        given(userService.accessUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/loginrequests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLoginDTO));

        // then
        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andReturn();
        String authentication = mvcResult.getResponse().getHeader("Authentication");
        assertEquals(authentication, user.getAuthentication());
    }

    @Test //post /loginrequests -> 400 : login with wrong password
    public void user_login_wrong_auth() throws Exception {
        // given predefined user
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword("wrongPassword");

        given(userService.accessUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password!"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/loginrequests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLoginDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(400));
    }


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}

     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The request body could not be created.");

        }
    }
}