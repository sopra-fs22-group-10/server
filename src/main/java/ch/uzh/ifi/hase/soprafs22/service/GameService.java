package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final SessionService sessionService;
    private final UserRepository userRepository;



    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("userRepository")UserRepository userRepository, SessionService sessionService) {
        this.gameRepository = gameRepository;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }

    public Game findGameByGameCode(Long gameCode) {
        Game foundGame = gameRepository.findByGameCode(gameCode);

        if(foundGame == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No game found with given game Code");
        }
        return foundGame;
    }

    public Game createGame(Long gameCode){
        //find corresponding Session
        Session foundSession = sessionService.getSessionByGameCode(gameCode.intValue());

        Game newGame = new Game();
        newGame.setGameCode(gameCode);
        newGame.setPlayerList(new ArrayList<Player>());

        newGame = addPlayers(newGame, foundSession);

        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        return newGame;
    }

    public Game gameUpdate(Long gameCode){
        return gameRepository.findByGameCode(gameCode);
    }




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
}



