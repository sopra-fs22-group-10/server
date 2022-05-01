package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import net.bytebuddy.matcher.FilterableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for the SessionResource REST resource.
 *
 * @see SessionService
 */
@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    @Qualifier("sessionRepository")
    @Autowired
    private SessionRepository sessionRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("deckRepository")
    @Autowired
    private DeckRepository deckRepository;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private GameService gameService;

    private User testUser1;
    private User testUser2;
    private Deck testDeck;
    private Session testSession;
    private Player testPlayer1;
    private Player testPlayer2;
    private Game testGame;

    @BeforeEach
    public void setup() {

        sessionRepository.deleteAll();
        userRepository.deleteAll();
        deckRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();

        testUser1 = new User();
        testUser1.setUsername("username");
        testUser1.setPassword("password");

        testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        User createdUser1 = userService.createUser(testUser1);
        User createdUser2 = userService.createUser(testUser2);


        testDeck = new Deck();
        testDeck.setDeckname("deck");

        Deck createdDeck = deckService.createDeck(testDeck);

        testSession = new Session();
        testSession.setMaxPlayers(2);
        testSession.setDeckId(createdDeck.getDeckId());
        testSession.setHostUsername(createdUser1.getUsername());
        testSession.setHostId(createdUser1.getUserId());

        Session createdSession = sessionService.createSession(testSession);
        sessionService.joinSessionByGameCode(createdSession.getGameCode(), testUser2.getUsername());

        testPlayer1 = new Player();
        testPlayer1.setPlayerId(createdUser1.getUserId());
        testPlayer1.setPlayerName(createdUser1.getUsername());
        testPlayer1.setPlayerStatus(PlayerStatus.ACTIVE);
        testPlayer1.setHand(new ArrayList<>());
        testPlayer1.setPlayedCards(new ArrayList<>());

        testPlayer2 = new Player();
        testPlayer2.setPlayerId(createdUser2.getUserId());
        testPlayer2.setPlayerName(createdUser2.getUsername());
        testPlayer2.setPlayerStatus(PlayerStatus.ACTIVE);
        testPlayer2.setHand(new ArrayList<>());
        testPlayer2.setPlayedCards(new ArrayList<>());

        testGame = new Game();
        testGame.setGameCode((long)createdSession.getGameCode());
        testGame.setCurrentPlayer(testUser1.getUserId());
        testGame.setOpponentPlayer(null);
        List<Player> playerList = new ArrayList<>();
        playerList.add(testPlayer1);
        playerList.add(testPlayer2);
        testGame.setPlayerList(playerList);
        testGame.setCurrentStatName(null);
        testGame.setRoundStatus(null);
        testGame.setWinner(null);
    }

    @AfterEach
    public void teardown(){
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        deckRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();
    }


    @Test
    public void createSessionSuccess() {

        // when
        Game createdGame = gameService.createGame(testGame.getGameCode());


        // then
        assertEquals(testGame.getGameCode(), createdGame.getGameCode());
        assertEquals(testGame.getCurrentPlayer(), createdGame.getCurrentPlayer());
        assertEquals(testGame.getOpponentPlayer(), createdGame.getOpponentPlayer());
        assertEquals(testGame.getPlayerList().get(0).getPlayerId(), createdGame.getPlayerList().get(0).getPlayerId());
        assertEquals(testGame.getPlayerList().get(0).getPlayerName(), createdGame.getPlayerList().get(0).getPlayerName());
        assertEquals(testGame.getPlayerList().get(1).getPlayerId(), createdGame.getPlayerList().get(1).getPlayerId());
        assertEquals(testGame.getPlayerList().get(1).getPlayerName(), createdGame.getPlayerList().get(1).getPlayerName());
        assertEquals(testGame.getRoundStatus(), createdGame.getRoundStatus());
        assertEquals(testGame.getCurrentStatName(), createdGame.getCurrentStatName());
        assertEquals(testGame.getWinner(), createdGame.getWinner());
    }
}

