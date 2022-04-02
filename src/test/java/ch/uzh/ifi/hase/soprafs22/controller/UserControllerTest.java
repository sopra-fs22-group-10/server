package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.LoginGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.pool.TypePool;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        testUser.setId(1L);

        testUser.setUsername("testUsername");
        testUser.setToken("1");
        String time = "2019-03-27T10:15:30";
        LocalDateTime localTimeObj = LocalDateTime.parse(time);
        testUser.setCreationDate(localTimeObj);
        testUser.setStatus(UserStatus.ONLINE);
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser

    }



    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();

        user.setId(1L);
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        user.setToken("1");

        user.setCreationDate(LocalDateTime.now());
        user.setStatus(UserStatus.ONLINE);

        user.setBirthdate(LocalDate.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].creationDate", is(formatter.format(user.getCreationDate()))));

    }

    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given


        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(testUser);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))

                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.status", is(testUser.getStatus().toString())));
    }

    @Test
    public void get_User_with_UserGet_with_Id() throws Exception{


        given(userService.getUserById(testUser.getId())).willReturn(testUser);

        // when/then -> do the request + validate the result
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())

                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.status", is(testUser.getStatus().toString())))
                .andExpect(jsonPath("$.creationDate", is(formatter.format(testUser.getCreationDate()))));

    }


    @Test //Get request to User but with wrong ID
    public void get_User_with_UserGet_with_wrong_Id() throws Exception{

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        String baseErrorMessage = "The %s provided does not exist. There %s not any user assotiated with this Id";
        given(userService.getUserById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "ID", "is")));

        MockHttpServletRequestBuilder getRequest = get("/users/2");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());


    }

    @Test
    public void change_Username_and_Birthday_with_Put_request_and_correct_User_Id() throws Exception{
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setToken("token1");
        userPutDTO.setUsername("testUsername");
        userPutDTO.setBirthdate("22/03/1998");

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        //given(userService.ChangeUserData(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).willReturn(null);


        mockMvc.perform(putRequest).andExpect(status().isNoContent());

    }
    @Test
    public void change_Username_and_Birthday_with_Put_request_and_wrong_User_Id() throws Exception{
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setToken("token1");
        userPutDTO.setUsername("testUsername");
        userPutDTO.setBirthdate("22/03/1998");

        MockHttpServletRequestBuilder putRequest = put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        String baseErrorMessage = "The %s provided does not exist. There %s not any user assotiated with this Id";
        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "ID", "is"))).given(userService).ChangeUserData(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());



        mockMvc.perform(putRequest).andExpect(status().isNotFound());

    }

    @Test
    public void successful_Login_to_previously_registered_account() throws Exception{



        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");

        given(userService.loginUsers(Mockito.any(), Mockito.any())).willReturn(testUser);

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.token", is(testUser.getToken())));





    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"password": "Test Password", "username": "testUsername"}
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