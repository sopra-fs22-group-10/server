package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SessionRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    private final DeckRepository deckRepository;

    private final PlayerService playerService;


    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,@Qualifier("sessionRepository") SessionRepository sessionRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("deckRepository") DeckRepository deckRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.playerService = playerService;
    }



    public Game createGame(Long gameCode){

        //create a Game and set the gameCode as Id
        Game newGame = new Game();
        newGame.setGameCode(gameCode);
        newGame.setPlayerList(new ArrayList<>());

        //find corresponding session
        Session session;
        try{
            session = sessionRepository.findByGameCode(newGame.getGameCode().intValue());
        } catch (ResponseStatusException e) {throw e; }

        //create a Player entity for each user in the session and store them in playerList
        addPlayers(newGame, session);

        //distribute Cards evenly to the players
        distributeCards(newGame, session);

        gameRepository.save(newGame);
        gameRepository.flush();
        return newGame;
    }
    public Game findGameByGameCode(Long gameCode) throws ResponseStatusException {
        Game foundGame = gameRepository.findByGameCode(gameCode);

        if(foundGame == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There exists no Game with given GameCode");
        }
        return foundGame;
    }


    //helper methods
    private void shuffle(List<Card> cardList){
        Collections.shuffle(cardList);
    }

    private void addPlayers(Game newGame, Session session){

        List<String> userList = session.getUserList();
        for (String s : userList) {
            User user = userRepository.findByUsername(s);
            Player createdPlayer = playerService.createPlayer(user.getUserId());
            newGame.addPlayer(createdPlayer);
        }
    }

    private void distributeCards(Game game, Session session){
        Deck deck = deckRepository.findByDeckId(session.getDeckId());

        List<Card> cardList = deck.getCardList();
        shuffle(cardList);
        List<Player> playerList = game.getPlayerList();

        //remove cards from deck until they can evenly be distributed
        while(cardList.size() % playerList.size() != 0){
            cardList = cardList.subList(0, cardList.size()- 2);
        }

        //cards can be distributed evenly
        for(int i = 0; i < cardList.size(); i++){
            int currentPlayerIndex = (cardList.size() % playerList.size());
            Player currentPlayer = playerList.get(currentPlayerIndex);
            playerService.addCardToHand(currentPlayer, cardList.get(i));
        }
    }
}

