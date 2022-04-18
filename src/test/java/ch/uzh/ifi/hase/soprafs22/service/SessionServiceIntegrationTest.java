package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SessionRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;

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

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("deckRepository")
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    @BeforeEach
    public void setup() {

        sessionRepository.deleteAll();
        userRepository.deleteAll();
        deckRepository.deleteAll();
    }


    @Test
    public void createSessionSuccess() {
        // given
        assertNull(sessionRepository.findBySessionId(1L));
        assertNull(userRepository.findByUserId(1L));
        assertNull(deckRepository.findByDeckId(1L));

        Session session = new Session();
        session.setMaxPlayers(1);
        session.setUserList(new ArrayList<String>());
        session.setDeckId(2L);
        session.setGameCode(1);
        session.setHostUsername("username");
        session.addUser("username");

        User user = new User();
        user.setUsername("username");
        user.setPassword("password");

        User createUser = userService.createUser(user);

        Deck deck = new Deck();
        deck.setDeckname("deck");

        Deck createDeck = deckService.createDeck(deck);

        // when
        Session createdSession = sessionService.createSession(session);

        // then
        assertEquals(session.getSessionId(), createdSession.getSessionId());
        assertEquals(session.getHostUsername(), createdSession.getHostUsername());
        assertEquals(session.getGameCode(), createdSession.getGameCode());
        assertEquals(session.getMaxPlayers(), createdSession.getMaxPlayers());
        assertEquals(session.getDeckId(), createdSession.getDeckId());
    }
}
