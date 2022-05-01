package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private GameService gameService;
    @Mock
    private SessionService sessionService;
    @Mock
    private Session testSession;
    @Mock
    private Game testGame;
    @Mock
    private Player testPlayer;
    @Mock
    private Player testPlayer2;
    @Mock
    private User testUser1;
    @Mock
    private User testUser2;
    @Mock
    private Card testCard1;
    @Mock
    private Card testCard2;
    @Mock
    private Deck testDeck;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser1 = new User();
        testUser1.setUsername("username");
        testUser1.setPassword("password");
        testUser1.setUserId(1L);

        testUser2 = new User();
        testUser2.setUsername("username");
        testUser2.setPassword("password");
        testUser2.setUserId(2L);

        testSession = new Session();
        testSession.setSessionId(1L);
        testSession.setMaxPlayers(2);
        testSession.setDeckId(testDeck.getDeckId());
        testSession.setGameCode(1);
        testSession.setHostUsername("username");
        testSession.setHostId(3L);
        List<String> userList = new ArrayList<>();
        userList.add(testUser1.getUsername());
        userList.add(testUser2.getUsername());
        testSession.setUserList(userList);

        testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckstatus(DeckStatus.PUBLIC);
        testDeck.setDeckname("testDeckname");
        List<Card> cardList = new ArrayList<>();
        cardList.add(testCard1);
        cardList.add(testCard2);
        testDeck.setCardList(cardList);


        testCard1 = new Card();
        testCard1.setCardId(1L);
        testCard1.setCardname("testCardname");
        testCard1.setImage("RandomImage");

        testCard2 = new Card();
        testCard2.setCardId(2L);
        testCard2.setCardname("testCardname");
        testCard2.setImage("RandomImage");

        testGame = new Game();
        testGame.setCurrentPlayer(1L);
        testGame.setOpponentPlayer(null);
        List<Player> playerList = new ArrayList<>();
        playerList.add(testPlayer);
        playerList.add(testPlayer2);
        testGame.setPlayerList(playerList);
        testGame.setRoundStatus(null);
        testGame.setCurrentStatName(null);
        testGame.setWinner(null);

        Mockito.when(sessionRepository.findByGameCode(Mockito.anyInt())).thenReturn(testSession);
        Mockito.when(gameRepository.findByGameCode(Mockito.anyLong())).thenReturn(testGame);
        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer);
        Mockito.when(sessionService.getSessionByGameCode(Mockito.anyInt())).thenReturn(testSession);
        Mockito.when(deckRepository.findByDeckId(testSession.getDeckId())).thenReturn(testDeck);
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser1);

        // when -> any object is being saved in the sessionRepository -> return the dummy
        // testGame
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);

    }

    @Test
    public void createGameSuccess() {

        // when -> any object is being saved in the sessionRepository -> return the dummy
        // testSession
        Game createdGame = gameService.createGame((long) testSession.getGameCode());


        // then
        Mockito.verify(gameRepository, Mockito.times(3)).save(Mockito.any());

        assertEquals(testGame.getGameCode(), createdGame.getGameCode());
        assertEquals(testGame.getCurrentPlayer(), createdGame.getCurrentPlayer());
        assertEquals(testGame.getOpponentPlayer(), createdGame.getOpponentPlayer());
        assertEquals(testGame.getPlayerList(), createdGame.getPlayerList());
        assertEquals(testGame.getRoundStatus(), createdGame.getRoundStatus());
        assertEquals(testGame.getCurrentStatName(), createdGame.getCurrentStatName());
        assertEquals(testGame.getWinner(), createdGame.getWinner());
    }

    /*
    @Test
    public void joinSessionSuccess() throws Exception {

        User testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        Session sessionToJoin = gameService.joinSessionByGameCode(testSession.getGameCode(), testUser2.getUsername());

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
        assertThrows(ResponseStatusException.class, () -> gameService.joinSessionByGameCode(0,testUser2.getUsername()));

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

        Session updatedSession = gameService.updateSession(inputSession);

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
        assertThrows(ResponseStatusException.class, () -> gameService.updateSession(inputSession));

    }*/
}
