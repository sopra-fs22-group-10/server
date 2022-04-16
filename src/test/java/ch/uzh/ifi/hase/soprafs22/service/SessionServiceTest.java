package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SessionRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session testSession;
    private User testUser;
    private Deck testDeck;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testSession = new Session();
        testSession.setSessionId(1L);
        testSession.setMaxPlayers(1);
        testSession.setDeckId(2L);
        testSession.setGameCode(1);
        testSession.setHostUsername("username");


        testUser = new User();
        testUser.setUsername("username");
        testUser.setPassword("password");

        testDeck = new Deck();
        testDeck.setDeckname("deck");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(deckRepository.findByDeckId(Mockito.any())).thenReturn(testDeck);
        // when -> any object is being saved in the sessionRepository -> return the dummy
        // testSession
        Mockito.when(sessionRepository.save(Mockito.any())).thenReturn(testSession);
    }

    @Test
    public void createSessionSuccess() {

        // when -> any object is being saved in the sessionRepository -> return the dummy
        // testSession
        Session createdSession = sessionService.createSession(testSession);


        // then
        Mockito.verify(sessionRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testSession.getSessionId(), createdSession.getSessionId());
        assertEquals(testSession.getHostUsername(), createdSession.getHostUsername());
        assertEquals(testSession.getGameCode(), createdSession.getGameCode());
        assertEquals(testSession.getMaxPlayers(), createdSession.getMaxPlayers());
        assertEquals(testSession.getDeckId(), createdSession.getDeckId());
        assertEquals(testSession.getUserList(), createdSession.getUserList());
    }

}
