package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
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

import static org.junit.jupiter.api.Assertions.*;

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
    private StatRepository statRepository;

    @Qualifier("cardRepository")
    @Autowired
    private CardRepository cardRepository;

    @Qualifier("templateRepository")
    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private GameService gameService;

    @Autowired
    private CardService cardService;

    private User testUser1;
    private User testUser2;
    private Deck testDeck;
    private Template testTemplate;
    private Stat templateStat;
    private Card testCard1;
    private Card testCard2;
    private Card testCard3;
    private Stat testStat1;
    private Stat testStat2;
    private Stat testStat3;
    private Session testSession;
    private Player testPlayer1;
    private Player testPlayer2;
    private Game testGame;

    @BeforeEach
    public void setup() {


        testUser1 = new User();
        testUser1.setUsername("username");
        testUser1.setPassword("password");

        testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password");

        User createdUser1 = userService.createUser(testUser1);
        User createdUser2 = userService.createUser(testUser2);

        testStat1 = new Stat();
        testStat1.setStatname("statName");
        testStat1.setStattype(StatTypes.NUMBER);
        testStat1.setStatvalue("100");

        testStat2 = new Stat();
        testStat2.setStatname("statName");
        testStat2.setStattype(StatTypes.NUMBER);
        testStat2.setStatvalue("1");

        testStat3 = new Stat();
        testStat3.setStatname("statName");
        testStat3.setStattype(StatTypes.NUMBER);
        testStat3.setStatvalue("50");

        templateStat = new Stat();
        templateStat.setStatname("statName");
        templateStat.setStattype(StatTypes.NUMBER);

        testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);

        testCard1 = new Card();
        testCard1.setCardname("testCard1");
        testCard1.setImage("someImage");
        List<Stat> cardStats1 = new ArrayList<>();
        cardStats1.add(testStat1);
        testCard1.setCardstats(cardStats1);

        testCard2 = new Card();
        testCard2.setCardname("testCard2");
        testCard2.setImage("someImage");
        List<Stat> cardStats2 = new ArrayList<>();
        cardStats2.add(testStat2);
        testCard2.setCardstats(cardStats2);

        testCard3 = new Card();
        testCard3.setCardname("testCard3");
        testCard3.setImage("someImage");
        List<Stat> cardStats3 = new ArrayList<>();
        cardStats3.add(testStat3);
        testCard3.setCardstats(cardStats3);

        testDeck = new Deck();
        testDeck.setDeckname("deck");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);

        Deck createdDeck = deckService.createDeck(testDeck);
        Card createdCard1 = cardService.createCard(testCard1,testTemplate);
        Card createdCard2 = cardService.createCard(testCard2,testTemplate);
        Card createdCard3 = cardService.createCard(testCard3, testTemplate);
        createdDeck = deckService.addNewCard(createdCard1, createdDeck.getDeckId());
        createdDeck = deckService.addNewCard(createdCard2, createdDeck.getDeckId());
        createdDeck = deckService.addNewCard(createdCard3, createdDeck.getDeckId());

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
        List<Card> hand1 = new ArrayList<>();
        hand1.add(testCard1);
        testPlayer1.setHand(hand1);


        testPlayer2 = new Player();
        testPlayer2.setPlayerId(createdUser2.getUserId());
        testPlayer2.setPlayerName(createdUser2.getUsername());
        testPlayer2.setPlayerStatus(PlayerStatus.ACTIVE);
        List<Card> hand2 = new ArrayList<>();
        hand2.add(testCard2);
        testPlayer2.setHand(hand2);


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
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        sessionRepository.deleteAll();
        deckRepository.deleteAll();
        templateRepository.deleteAll();
        cardRepository.deleteAll();
        statRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void createGameSuccess() {

        // when
        Game createdGame = gameService.createGame(testGame.getGameCode());


        // then
        assertEquals(testGame.getGameCode(), createdGame.getGameCode());
        assertEquals(testGame.getCurrentPlayer(), createdGame.getCurrentPlayer());
        assertEquals(testGame.getOpponentPlayer(), createdGame.getOpponentPlayer());
        assertEquals(testGame.getPlayerList().get(0).getPlayerId(), createdGame.getPlayerList().get(0).getPlayerId());
        assertEquals(testGame.getPlayerList().get(0).getPlayerName(), createdGame.getPlayerList().get(0).getPlayerName());
        assertEquals(testGame.getPlayerList().get(0).getPlayerStatus(), createdGame.getPlayerList().get(0).getPlayerStatus());
        assertEquals(testGame.getPlayerList().get(1).getPlayerId(), createdGame.getPlayerList().get(1).getPlayerId());
        assertEquals(testGame.getPlayerList().get(1).getPlayerName(), createdGame.getPlayerList().get(1).getPlayerName());
        assertEquals(testGame.getPlayerList().get(0).getPlayerStatus(), createdGame.getPlayerList().get(0).getPlayerStatus());
        assertEquals(testGame.getRoundStatus(), createdGame.getRoundStatus());
        assertEquals(testGame.getCurrentStatName(), createdGame.getCurrentStatName());
        assertEquals(testGame.getWinner(), createdGame.getWinner());

        //since the cards get shuffled before they get distributed we only know
        //that they have the same statName and a different statValue
        //but don't know which player has which card in his hand

        //both players should have only 1 card in their hand
        // meaning that the 3 cards of the deck where distributed evenly
        Player player1 = createdGame.getPlayerList().get(0);
        Player player2 = createdGame.getPlayerList().get(1);
        assertEquals(player1.getHand().size(), 1);
        assertEquals(player1.getHand().size(), player2.getHand().size());

        //the cards must have the same statName but a different statValue
        Card cardPlayer1 = player1.getHand().get(0);
        Card cardPlayer2 = player2.getHand().get(0);
        Stat statPlayer1 = cardPlayer1.getCardstats().get(0);
        Stat statPlayer2 = cardPlayer2.getCardstats().get(0);

        assertEquals(statPlayer1.getStatname(), statPlayer2.getStatname());
        assertNotEquals(statPlayer1.getStatvalue(), statPlayer2.getStatvalue());

    }

    @Test
    public void findGameByGameCodeSuccess(){

        Game game = gameService.createGame(testGame.getGameCode());

        Game foundGame = gameService.findGameByGameCode(testGame.getGameCode());

        assertEquals(foundGame.getGameCode(), game.getGameCode());
    }

    @Test
    public void findGameByGameCodeFail() throws ResponseStatusException{
        assertThrows(ResponseStatusException.class, () -> gameService.findGameByGameCode(testGame.getGameCode()));
    }

    @Test
    public void wrongGameCode() throws ResponseStatusException{
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(0L));
    }

    @Test
    public void createGameWithExistingGameCode() throws ResponseStatusException{
        Game createdGame = gameService.createGame((long)testSession.getGameCode());
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(createdGame.getGameCode()));
    }

    @Test
    public void updateGameWhenChoosingOpponent(){

        Game createdGame = gameService.createGame(testGame.getGameCode());

        assertNull(createdGame.getOpponentPlayer());
    }

    @Test
    public void updateOpponentPlayer(){
        Game createdGame = gameService.createGame(testGame.getGameCode());

        Game updatedGame = gameService.gameUpdate(createdGame.getGameCode(), testPlayer2.getPlayerId(),null);

        assertEquals(testPlayer2.getPlayerId(), updatedGame.getOpponentPlayer());
    }

    @Test
    public void playRound(){
        Game createdGame = gameService.createGame(testGame.getGameCode());

        Game updatedGame = gameService.gameUpdate(createdGame.getGameCode(), testPlayer2.getPlayerId(),null);

        updatedGame = gameService.gameUpdate(createdGame.getGameCode(), null, "statName");

        assertNotNull(updatedGame.getWinner());
    }

}

