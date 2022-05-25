package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.JoinSessionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.SessionService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Join;
import org.hibernate.mapping.JoinedSubclass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SessionControllerTest
 * This is a WebMvcTest which allows to test the SessionController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the SessionController works.
 */
@WebMvcTest(SessionController.class)
public class SessionControllerTest {
    Session session;
    User user;
    SessionGetDTO sessionGetDTO;

    @BeforeEach
    public void init() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setAuthentication("auth");
        user.setStatus(UserStatus.ONLINE);

        session = new Session();
        session.setSessionId(1L);
        session.setMaxPlayers(2);
        session.setUserList(new ArrayList<String>());
        session.setDeckId(1L);
        session.setGameCode(1);
        session.setHostUsername(user.getUsername());
        session.setHostId(user.getUserId());
        session.addUser(user.getUsername());

    }

    @AfterEach
    public void teardown() {
        sessionService.deleteSessionById(session.getSessionId());
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;
    @MockBean
    private UserService userService;

    @Test //post /session -> 201 : successful creation of session
    public void createSession_validInput_SessionCreated() throws Exception {
        // given predefined session
        SessionPostDTO sessionPostDTO = new SessionPostDTO();
        sessionPostDTO.setHostUsername(session.getHostUsername());
        sessionPostDTO.setDeckId(session.getDeckId());
        sessionPostDTO.setMaxPlayers(session.getMaxPlayers());
        given(sessionService.createSession(Mockito.any())).willReturn(session);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(sessionPostDTO));
        // then
        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.gameCode", is(session.getGameCode())))
                .andExpect(jsonPath("$.hostUsername", is(session.getHostUsername())))
                .andExpect(jsonPath("$.hostId", is(session.getHostId().intValue())))
                .andExpect(jsonPath("$.userList", is(session.getUserList())))
                .andExpect(jsonPath("$.deckId", is(session.getDeckId().intValue())))
                .andExpect(jsonPath("$.maxPlayers", is(session.getMaxPlayers())))
                .andReturn();
    }

    @Test //get /session/1 -> 200 : successfully retrieve data for session with ID 1
    public void getSessionByGameCode() throws Exception {
        // given predfined session
        // this mocks the SessionService -> we define above what the sessionService should return when getSessionByGameCode() is called
        given(sessionService.getSessionByGameCode(session.getGameCode())).willReturn(session);

        // when
        MockHttpServletRequestBuilder getRequest = get("/session/"+session.getGameCode()).contentType(MediaType.APPLICATION_JSON).header("Authentication", user.getAuthentication());

        // then
        mockMvc.perform(getRequest).andExpect(status().is(200))
                .andExpect(jsonPath("$.gameCode", is(session.getGameCode())));
    }

    @Test //post /session/create -> 404: deck not found
    public void createSessionWithWrongDeckId() throws Exception{
        // given predefined session
        SessionPostDTO sessionPostDTO = new SessionPostDTO();
        sessionPostDTO.setHostUsername(session.getHostUsername());
        sessionPostDTO.setHostId(session.getHostId());
        sessionPostDTO.setDeckId(9L);
        sessionPostDTO.setMaxPlayers(session.getMaxPlayers());
        given(sessionService.createSession(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.resolve(404), "Given Deck does not exist"));
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(sessionPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(404));
    }

    @Test //get /session/1 -> 404 : fail at retrieving data because session 1 doesnt exist
    public void getSessionWrongGameCode() throws Exception {
        // given predfined session
        // this mocks the SessionService -> we define above what the sessionService should return when getSessionByGameCode() is called
        given(sessionService.getSessionByGameCode(session.getGameCode())).willThrow(new ResponseStatusException(HttpStatus.resolve(404),
                "No session for this GameCode found"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/session/"+session.getGameCode()).contentType(MediaType.APPLICATION_JSON).header("Authentication", user.getAuthentication());

        // then
        mockMvc.perform(getRequest).andExpect(status().is(404));
    }

    @Test //post /session/create -> 400: Max number of players is 6
    public void createSessionWithToManyPlayers() throws Exception{
        // given predefined session
        SessionPostDTO sessionPostDTO = new SessionPostDTO();
        sessionPostDTO.setHostUsername(session.getHostUsername());
        sessionPostDTO.setHostId(session.getHostId());
        sessionPostDTO.setDeckId(session.getDeckId());
        sessionPostDTO.setMaxPlayers(9);
        given(sessionService.createSession(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.resolve(400), "Maximum number of players is 6!"));
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(sessionPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(400));
    }


    @Test //Post /session/join/{gameCode} -> 200 : successfully join session and get Updated Session back
    public void joinSessionSuccess() throws Exception {
        // given predfined session
        JoinSessionPostDTO joinSessionPostDTO = new JoinSessionPostDTO();
        joinSessionPostDTO.setUsername("username2");
        joinSessionPostDTO.setUserId(2L);

        session.addUser(joinSessionPostDTO.getUsername());
        // this mocks the SessionService -> we define above what the sessionService should return when getSessionByGameCode() is called
        given(sessionService.joinSessionByGameCode(Mockito.anyInt(),Mockito.anyString())).willReturn(session);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/join/"+session.getGameCode())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(joinSessionPostDTO));

        // then
        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.gameCode", is(session.getGameCode())))
                .andExpect(jsonPath("$.hostUsername", is(session.getHostUsername())))
                .andExpect(jsonPath("$.hostId", is(session.getHostId().intValue())))
                .andExpect(jsonPath("$.userList", is(session.getUserList())))
                .andExpect(jsonPath("$.deckId", is(session.getDeckId().intValue())))
                .andExpect(jsonPath("$.maxPlayers", is(session.getMaxPlayers())))
                .andReturn();
    }

    @Test //Post /session/join/{gameCode} -> 400 : fail to join session since session is already full
    public void joinSessionWhereSessionIsFull() throws Exception {
    // given predfined session
        JoinSessionPostDTO joinSessionPostDTO = new JoinSessionPostDTO();
        joinSessionPostDTO.setUsername("username2");
        joinSessionPostDTO.setUserId(2L);

        session.addUser(joinSessionPostDTO.getUsername());
        // this mocks the SessionService -> we define above what the sessionService should return when getSessionByGameCode() is called
        given(sessionService.joinSessionByGameCode(Mockito.anyInt(), Mockito.anyString())).willThrow(new ResponseStatusException(HttpStatus.resolve(400), "The Session is already full"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/join/"+session.getGameCode())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(joinSessionPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(400));
    }

    @Test //Post /session/join/{gameCode} -> 404 : fail to join session since Session does not exist
    public void joinSessionWithInvalidGameCode() throws Exception{
        // given predfined session
        JoinSessionPostDTO joinSessionPostDTO = new JoinSessionPostDTO();
        joinSessionPostDTO.setUsername("username2");
        joinSessionPostDTO.setUserId(2L);


        // this mocks the SessionService -> we define above what the sessionService should return when getSessionByGameCode() is called
        given(sessionService.joinSessionByGameCode(Mockito.anyInt(), Mockito.anyString())).willThrow(new ResponseStatusException(HttpStatus.resolve(404), "There exists no Session with given gameCode"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/join/"+77)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(joinSessionPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(404));

    }

    @Test //Post /session/join/{gameCode} -> 404 : fail to join session since given User does not exist
    public void joinSessionWhitUserNotExisting() throws Exception{
        // given predfined session
        JoinSessionPostDTO joinSessionPostDTO = new JoinSessionPostDTO();
        joinSessionPostDTO.setUsername("username2");
        joinSessionPostDTO.setUserId(2L);


        // this mocks the SessionService -> we define above what the sessionService should return when getSessionByGameCode() is called
        given(sessionService.joinSessionByGameCode(Mockito.anyInt(), Mockito.anyString())).willThrow(new ResponseStatusException(HttpStatus.resolve(404), "There exists no User with given username"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/join/"+session.getGameCode())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(joinSessionPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(404));

    }

    @Test //Put /session/{gameCode} -> 200 : successfully updated session gets returned
    public void updateSessionSuccess() throws Exception {
        // given predefined session
        SessionPostDTO sessionPostDTO = new SessionPostDTO();
        sessionPostDTO.setHostUsername(session.getHostUsername());
        sessionPostDTO.setHostId(session.getHostId());
        sessionPostDTO.setDeckId(session.getDeckId());
        sessionPostDTO.setMaxPlayers(session.getMaxPlayers()+1);

        given(sessionService.updateSession(Mockito.any())).willReturn(session);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = put("/session/"+session.getGameCode())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(sessionPostDTO));

        // then
        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.gameCode", is(session.getGameCode())))
                .andExpect(jsonPath("$.hostUsername", is(session.getHostUsername())))
                .andExpect(jsonPath("$.hostId", is(session.getHostId().intValue())))
                .andExpect(jsonPath("$.userList", is(session.getUserList())))
                .andExpect(jsonPath("$.deckId", is(session.getDeckId().intValue())))
                .andExpect(jsonPath("$.maxPlayers", is(session.getMaxPlayers())))
                .andReturn();
    }

    @Test //Put /session/{gameCode} -> 404: session to be updated does not exist
    public void updateSessionWithInvalidGameCode() throws Exception {
// given predefined session
        SessionPostDTO sessionPostDTO = new SessionPostDTO();
        sessionPostDTO.setHostUsername(session.getHostUsername());
        sessionPostDTO.setHostId(session.getHostId());
        sessionPostDTO.setDeckId(session.getDeckId());
        sessionPostDTO.setMaxPlayers(session.getMaxPlayers()+1);

        given(sessionService.updateSession(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "There exists no Session with given gamecode"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = put("/session/"+0)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(sessionPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(404));
    }

    @Test //Put /session/{gameCode} -> 404: Deck to be updated does not exist
    public void updateSessionWithInvalidInformation() throws Exception {
        // given predefined session
        SessionPostDTO sessionPostDTO = new SessionPostDTO();
        sessionPostDTO.setHostUsername(session.getHostUsername());
        sessionPostDTO.setHostId(session.getHostId());
        sessionPostDTO.setDeckId(0L);
        sessionPostDTO.setMaxPlayers(session.getMaxPlayers());

        given(sessionService.updateSession(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "There exists no Deck with given DeckId"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = put("/session/"+session.getGameCode())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
                .content(asJsonString(sessionPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(404));
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
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }
}