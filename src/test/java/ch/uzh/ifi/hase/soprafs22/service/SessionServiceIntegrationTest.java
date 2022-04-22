package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.SessionRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test class for the SessionResource REST resource.
 *
 * @see SessionService
 */
@WebAppConfiguration
@SpringBootTest
public class SessionServiceIntegrationTest {

    @Qualifier("sessionRepository")
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    private User testUser;
    private Deck testDeck;
    private Session testSession;


    @BeforeEach
    public void setup() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        deckRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("username");
        testUser.setPassword("password");

        User createdUser = userService.createUser(testUser);


        testDeck = new Deck();
        testDeck.setDeckname("deck");

        Deck createdDeck = deckService.createDeck(testDeck);

        testSession = new Session();
        testSession.setMaxPlayers(2);
        testSession.setUserList(new ArrayList<String>());
        testSession.setDeckId(createdDeck.getDeckId());
        testSession.setGameCode(1);
        testSession.setHostUsername(createdUser.getUsername());
        testSession.addUser(createdUser.getUsername());

    }

    @AfterEach
    public void teardown(){
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        deckRepository.deleteAll();
    }

    @Test
    public void createSessionSuccess() {

        // when
        Session createdSession = sessionService.createSession(testSession);


        // then
        assertEquals(testSession.getSessionId(), createdSession.getSessionId());
        assertEquals(testSession.getHostUsername(), createdSession.getHostUsername());
        assertEquals(testSession.getGameCode(), createdSession.getGameCode());
        assertEquals(testSession.getMaxPlayers(), createdSession.getMaxPlayers());
        assertEquals(testSession.getDeckId(), createdSession.getDeckId());
        assertEquals(testSession.getUserList(), createdSession.getUserList());
    }

    @Test
    public void createSessionWrongDeck() {
        //try to create a session with nonExisting Deck
        Session testSession2 = testSession;
        testSession2.setDeckId(testDeck.getDeckId() + testDeck.getDeckId());

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.createSession(testSession2));
    }

    @Test
    public void createSessionWrongUser() {
        //try to create a session with nonExisting Deck
        Session testSession2 = testSession;
        testSession2.setHostUsername("WrongUsername");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.createSession(testSession2));
    }

    @Test
    public void joinSessionSuccess(){
        //create second testUser which should join the Session
        User testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        User createdUser2 = userService.createUser(testUser2);

        //create a session to join
        Session createdSession = sessionService.createSession(testSession);
        //createdSession.addUser(testUser2.getUsername());

        //when
        Session joinedSession = sessionService.joinSessionByGameCode(createdSession.getGameCode(), createdUser2.getUsername());
        createdSession = sessionService.getSessionByGameCode(createdSession.getGameCode());

        // then
        assertEquals(createdSession.getSessionId(), joinedSession.getSessionId());
        assertEquals(createdSession.getHostUsername(), joinedSession.getHostUsername());
        assertEquals(createdSession.getGameCode(), joinedSession.getGameCode());
        assertEquals(createdSession.getMaxPlayers(), joinedSession.getMaxPlayers());
        assertEquals(createdSession.getDeckId(), joinedSession.getDeckId());
        assertEquals(createdSession.getUserList().size(), joinedSession.getUserList().size());
    }

    @Test
    public void joinSessionWrongGameCode() {
        //create second testUser which should join the Session
        User testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        User createdUser2 = userService.createUser(testUser2);

        //create a session to join
        Session createdSession = sessionService.createSession(testSession);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.joinSessionByGameCode(0, createdUser2.getUsername()));
    }

    @Test
    public void joinSessionWhenSessionIsFull() {

        //create second testUser which should join the Session
        User testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        User createdUser2 = userService.createUser(testUser2);

        //create a session to join
        Session createdSession = sessionService.createSession(testSession);
        Session sessionToJoin = sessionService.joinSessionByGameCode(createdSession.getGameCode(), createdUser2.getUsername());

        //create third testUser which should not be able to join the Session
        User testUser3 = new User();
        testUser3.setUsername("username3");
        testUser3.setPassword("password");

        User createdUser3 = userService.createUser(testUser3);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.joinSessionByGameCode(sessionToJoin.getGameCode(), createdUser3.getUsername()));
    }

    @Test
    public void updateSessionSuccess() throws Exception {
        //create a session to update
        Session existingSession = sessionService.createSession(testSession);

        //create a Session with updated details (maxPlayers +1)
        Session sessionInput = new Session();
        sessionInput.setGameCode(existingSession.getGameCode());
        sessionInput.setSessionId(existingSession.getSessionId());
        sessionInput.setHostUsername(existingSession.getHostUsername());
        sessionInput.setMaxPlayers(existingSession.getMaxPlayers() + 1);
        sessionInput.setUserList(existingSession.getUserList());
        sessionInput.setDeckId(existingSession.getDeckId());


        Session updatedSession = sessionService.updateSession(sessionInput);
        existingSession = sessionService.getSessionByGameCode(existingSession.getGameCode());

        // then

        assertEquals(existingSession.getSessionId(), updatedSession.getSessionId());
        assertEquals(existingSession.getHostUsername(), updatedSession.getHostUsername());
        assertEquals(existingSession.getGameCode(), updatedSession.getGameCode());
        assertEquals(existingSession.getMaxPlayers(), updatedSession.getMaxPlayers());
        assertEquals(existingSession.getDeckId(), updatedSession.getDeckId());
        assertEquals(existingSession.getUserList().size(), updatedSession.getUserList().size());

    }

    @Test
    public void updateSessionInvalidGameCode() throws Exception {
        //create a session to update
        Session existingSession = sessionService.createSession(testSession);

        //create a Session with updated details (maxPlayers +1)
        Session sessionInput = new Session();
        sessionInput.setGameCode(0);
        sessionInput.setSessionId(existingSession.getSessionId());
        sessionInput.setHostUsername(existingSession.getHostUsername());
        sessionInput.setMaxPlayers(existingSession.getMaxPlayers() + 1);
        sessionInput.setUserList(existingSession.getUserList());
        sessionInput.setDeckId(existingSession.getDeckId());


        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.updateSession(sessionInput));
    }

    @Test //wrong deckID
    public void updateSessionInvalidInformation() throws Exception {
        //create a session to update
        Session existingSession = sessionService.createSession(testSession);

        //create a Session with updated details (maxPlayers +1)
        Session sessionInput = new Session();
        sessionInput.setGameCode(existingSession.getGameCode());
        sessionInput.setSessionId(existingSession.getSessionId());
        sessionInput.setHostUsername(existingSession.getHostUsername());
        sessionInput.setMaxPlayers(existingSession.getMaxPlayers());
        sessionInput.setUserList(existingSession.getUserList());
        sessionInput.setDeckId(0L);


        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.updateSession(sessionInput));
    }


}

