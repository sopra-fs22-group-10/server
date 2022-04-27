package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final GameRepository gameRepository;

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    private final DeckRepository deckRepository;

    private final PlayerRepository playerRepository;


    @Autowired
    public PlayerService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("sessionRepository") SessionRepository sessionRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("deckRepository") DeckRepository deckRepository, @Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.playerRepository = playerRepository;
    }


    public Player createPlayer(Long userId){
        User foundUser;
        try {
            foundUser = userRepository.findByUserId(userId);
        } catch (ResponseStatusException e) {throw e;}

        Player newPlayer = new Player();
        newPlayer.setPlayerId(foundUser.getUserId());
        newPlayer.setUsername(foundUser.getUsername());
        newPlayer.setPlayerStatus(PlayerStatus.ACTIVE);

        //create the two cardLists "hand" and "playedCards"
        LinkedList<Card> hand = new LinkedList<>();
        LinkedList<Card> playedCards = new LinkedList<>();

        newPlayer.addList(hand);
        newPlayer.addList(playedCards);

        playerRepository.save(newPlayer);
        playerRepository.flush();

        return newPlayer;

    }
    public Player findByPlayerId(Long playerId) throws ResponseStatusException {
        Player foundPlayer = playerRepository.findByPlayerId(playerId);

        if(foundPlayer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There exists no Player with given PlayerId");
        }
        return foundPlayer;
    }

    public void addCardToHand(Player player, Card card){
        LinkedList<Card> hand = player.getCards().get(0);
        hand.add(card);
    }


    //helper methods

}

