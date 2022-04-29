package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SessionRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final DeckRepository deckRepository;



    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("userRepository")UserRepository userRepository, @Qualifier("deckRepository") DeckRepository deckRepository, SessionService sessionService) {
        this.gameRepository = gameRepository;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
    }

    public Game findGameByGameCode(Long gameCode) {
        Game foundGame = gameRepository.findByGameCode(gameCode);

        if(foundGame == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No game found with given game Code");
        }
        return foundGame;
    }

    public Game createGame(Long gameCode){
        //check if game already exists
        try{
            checkIfGameExists(gameCode);
        } catch (ResponseStatusException e) { throw e; }
        //find corresponding Session
        Session foundSession = sessionService.getSessionByGameCode(gameCode.intValue());


        Game newGame = new Game();
        newGame.setGameCode(gameCode);
        newGame.setPlayerList(new ArrayList<Player>());

        newGame = addPlayers(newGame, foundSession);
        distributeCards(newGame, foundSession);

        //set player who begins to current player
        newGame.setCurrentPlayer(newGame.getPlayerList().get(0).getPlayerId());
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        sessionService.checkIfSessionHasGame(foundSession);
        return newGame;
    }
/*
    public Game gameUpdate(Long opponentPlayer, String currentStatName){
        //The gameUpdate gets a PutDTO with opponentPlayer[id] and currentStatName [string]
        //The method should check which of the input gets updated and act accordingly
        //The method should set the opponent playerId when requested
        //after that the game should return (only opponent player changes)

        //when only currentStatName gets changed:
        //take the cards from hand and move them to playedCards
        //The method should compare the currentStats from both currentPlayerHand and define wether theres a winner or draw
        //draw both players should draw a new card from hand
        //winner should be given all the cards and then the Roundstatus gets updated

        if(currentStatName == null && opponentPlayer != null){
            updateOpponentPlayer(opponentPlayer);
        }

        if(currentStatName != null && opponentPlayer == null){
            //playRound(currentStatName);
        }

        //else there should be errorChecks




    }*/




    //helper methods
    private Game addPlayers(Game game, Session session){

        List<String> allUsers = session.getUserList();
        List<Player> playerList = game.getPlayerList();

        //for each user in userList a new Player should be created and added to playerList
        for(String username : allUsers){
            User user = userRepository.findByUsername(username);
            Player playerToAdd = createPlayer(game, user);
            playerList.add(playerToAdd);

            //save the entity
            game = gameRepository.save(game);
            gameRepository.flush();
        }
        return game;
    }

    private Player createPlayer(Game game, User user){
        Player newPlayer = new Player();

        newPlayer.setPlayerId(user.getUserId());
        newPlayer.setPlayerName(user.getUsername());
        newPlayer.setPlayerStatus(PlayerStatus.ACTIVE);
        newPlayer.setHand(new ArrayList<Card>());
        newPlayer.setPlayedCards(new ArrayList<Card>());
        //newPlayer.setGame(game);

        return newPlayer;
    }

    private void distributeCards(Game game, Session session){
        Deck deck = deckRepository.findByDeckId(session.getDeckId());

        List<Card> cardList = deck.getCardList();
        Collections.shuffle(cardList);
        List<Player> playerList = game.getPlayerList();

        //remove cards from deck until they can evenly be distributed
        while(cardList.size() % playerList.size() != 0){
            cardList = cardList.subList(0, cardList.size()- 1);
        }

        //cards can be distributed evenly
        for(int i = 0; i < cardList.size(); i++){
            int currentPlayerIndex = (i % playerList.size());
            Player currentPlayer = playerList.get(currentPlayerIndex);
            Card currentCard = cardList.get(i);

            List<Card> hand = currentPlayer.getHand();
            hand.add(currentCard);
            currentPlayer.setHand(hand);
        }

    }
    private void checkIfGameExists(Long gameCode){
        Game foundGame = gameRepository.findByGameCode(gameCode);

        if(foundGame != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There already exists a Game with given gameCode");
        }
    }
}



