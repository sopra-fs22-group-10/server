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
        testSession.setGameCode(123456);
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


}
