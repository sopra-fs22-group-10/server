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
import java.util.ArrayList;
import java.util.Optional;

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
        testSession.setMaxPlayers(2);
        testSession.setDeckId(2L);
        testSession.setGameCode(1);
        testSession.setHostUsername("username");
        testSession.setHostId(3L);
        testSession.setUserList(new ArrayList<>());

        testUser = new User();
        testUser.setUsername("username");
        testUser.setPassword("password");

        testDeck = new Deck();
        testDeck.setDeckname("deck");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(deckRepository.findByDeckId(Mockito.any())).thenReturn(testDeck);
        Mockito.when(sessionRepository.findByGameCode(Mockito.anyInt())).thenReturn(testSession);

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
        assertEquals(testSession.getHostId(), createdSession.getHostId());
        assertEquals(testSession.getGameCode(), createdSession.getGameCode());
        assertEquals(testSession.getMaxPlayers(), createdSession.getMaxPlayers());
        assertEquals(testSession.getDeckId(), createdSession.getDeckId());
        assertEquals(testSession.getUserList(), createdSession.getUserList());
    }

    @Test
    public void joinSessionSuccess() throws Exception {

        User testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        Session sessionToJoin = sessionService.joinSessionByGameCode(testSession.getGameCode(), testUser2.getUsername());

        assertEquals(testSession.getSessionId(), sessionToJoin.getSessionId());
        assertEquals(testSession.getHostUsername(), sessionToJoin.getHostUsername());
        assertEquals(testSession.getHostId(), sessionToJoin.getHostId());
        assertEquals(testSession.getGameCode(), sessionToJoin.getGameCode());
        assertEquals(testSession.getMaxPlayers(), sessionToJoin.getMaxPlayers());
        assertEquals(testSession.getDeckId(), sessionToJoin.getDeckId());
        assertEquals(testSession.getUserList(), sessionToJoin.getUserList());

    }

    @Test
    public void joinSessionInvalidGameCode() throws Exception {

        User testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        //throw an error when trying to find the Session with given gamecode
        Mockito.when(sessionRepository.findByGameCode(Mockito.anyInt())).thenThrow(ResponseStatusException.class);

        // then -> attempt to update a session-> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.joinSessionByGameCode(0,testUser2.getUsername()));

    }

    @Test
    public void updateSessionSuccess() throws Exception {
        //create inputSession which contains the changes for session (maxPlayers +1)
        Session inputSession = new Session();
        inputSession.setMaxPlayers(testSession.getMaxPlayers()+1);
        inputSession.setDeckId(testSession.getDeckId());
        inputSession.setGameCode(testSession.getGameCode());
        inputSession.setHostUsername(testSession.getHostUsername());
        inputSession.setHostId(testSession.getHostId());

        Session updatedSession = sessionService.updateSession(inputSession);

        //then verify
        assertEquals(testSession.getSessionId(), updatedSession.getSessionId());
        assertEquals(testSession.getHostUsername(), updatedSession.getHostUsername());
        assertEquals(testSession.getHostId(), updatedSession.getHostId());
        assertEquals(testSession.getGameCode(), updatedSession.getGameCode());
        assertEquals(testSession.getMaxPlayers(), updatedSession.getMaxPlayers());
        assertEquals(testSession.getDeckId(), updatedSession.getDeckId());
        assertEquals(testSession.getUserList(), updatedSession.getUserList());
    }

    @Test
    public void updateSessionInvalidGameCode() throws Exception {
        //create inputSession which contains the changes for session (maxPlayers +1)
        Session inputSession = new Session();
        inputSession.setMaxPlayers(testSession.getMaxPlayers()+1);
        inputSession.setDeckId(testSession.getDeckId());
        inputSession.setGameCode(testSession.getGameCode());
        inputSession.setHostUsername(testSession.getHostUsername());
        inputSession.setHostId(testSession.getHostId());

        //throw an error when trying to find the Session with given gamecode
        Mockito.when(sessionRepository.findByGameCode(Mockito.anyInt())).thenThrow(ResponseStatusException.class);

        // then -> attempt to update a session-> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> sessionService.updateSession(inputSession));

    }
}
