package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs22.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
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
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("userRepository")UserRepository userRepository, @Qualifier("deckRepository") DeckRepository deckRepository,@Qualifier("playerRepository") PlayerRepository playerRepository, SessionService sessionService) {
        this.gameRepository = gameRepository;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.playerRepository = playerRepository;
    }

    public Game findGameByGameCode(Long gameCode) {
        Game foundGame = gameRepository.findByGameCode(gameCode);

        if(foundGame == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No game found with given game Code");
        }
        return foundGame;
    }

    public void deleteGameByGameCode (Long gameCode, User user)throws ResponseStatusException{
        Session foundSession = sessionService.getSessionByGameCode(gameCode.intValue());
        if(!user.getUsername().equals(foundSession.getHostUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the host can end a game");
        }

        foundSession.setHasGame(false);

        gameRepository.deleteByGameCode(gameCode);
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
    @Transactional
    public Game gameUpdate(Long gameCode, Long opponentPlayer, String currentStatName){
        //The gameUpdate gets a PutDTO with updated opponentPlayer[id] or currentStatName [string]
        //The method should check which of the input gets updated and act accordingly
        //The method should set the opponent playerId when requested
        //take the cards from hand and move them to playedCards
        //after that the game should return (opponentPlayer and hand/playedCards should change)

        //when only currentStatName gets changed:
        //The method should compare the currentStats from both currentPlayerHand and define wether theres a winner or draw
        //draw both players should draw a new card from hand
        //winner should be given all the cards and then the Roundstatus gets updated
        Game game;
        try{
            game = findGameByGameCode(gameCode);
        } catch (ResponseStatusException e) { throw e;}

        if(currentStatName == null && opponentPlayer != null){
            updateOpponentPlayer(game, opponentPlayer);
        }

        if(currentStatName != null && opponentPlayer == null){
            playRound(game,currentStatName);
        }

        //else there should be errorChecks

        return game;
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

    private void checkIfGameExists(Long gameCode) throws ResponseStatusException{
        Game foundGame = gameRepository.findByGameCode(gameCode);

        if(foundGame != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There already exists a Game with given gameCode");
        }
    }

    private void updateOpponentPlayer(Game game, Long opponentPlayerId) throws ResponseStatusException{
        Player opponentPlayer = playerRepository.findByPlayerId(opponentPlayerId);

        if(opponentPlayer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There exists no player with given playerId");
        }

        if(opponentPlayer.getPlayerStatus() == PlayerStatus.INACTIVE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This opponent has no cards left [PlayerStatus = INACTIVE]");
        }

        if(opponentPlayerId == game.getCurrentPlayer()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The opponent and current Player cannot have the same Id");
        }

        game.setOpponentPlayer(opponentPlayerId);

        //now the first card of both's hand will be appended in playedCards
        Player currentPlayer = playerRepository.findByPlayerId(game.getCurrentPlayer());

        List<Card> currentPlayerHand = currentPlayer.getHand();
        List<Card> opponentPlayerHand = opponentPlayer.getHand();

        List<Card> currentPlayerPlayedCards = currentPlayer.getPlayedCards();
        List<Card> opponentPlayerPlayedCards = opponentPlayer.getPlayedCards();

        currentPlayerPlayedCards.add(currentPlayerHand.remove(0));
        opponentPlayerPlayedCards.add(opponentPlayerHand.remove(0));

        currentPlayer.setHand(currentPlayerHand);
        currentPlayer.setPlayedCards(currentPlayerPlayedCards);
        opponentPlayer.setHand(opponentPlayerHand);
        opponentPlayer.setPlayedCards(opponentPlayerPlayedCards);

        game.setCurrentStatName(null);
        game.setRoundStatus(null);

        game = gameRepository.save(game);
        gameRepository.flush();
    }

    private void playRound(Game gameInput, String currentStatName) {
        Game game = gameInput;
        game.setCurrentStatName(currentStatName);
        Player currentPlayer = playerRepository.findByPlayerId(game.getCurrentPlayer());
        Player opponentPlayer = playerRepository.findByPlayerId(game.getOpponentPlayer());

        //get the two stats to compare [Always take the last Card in playedCards this way
        //if there is a draw this should be the most recent one]
        List<Card> currentPlayerPlayedCards = currentPlayer.getPlayedCards();
        List<Card> opponentPlayerPlayedCards = opponentPlayer.getPlayedCards();

        //retrieve the last card of playedCard from both players
        Card currentPlayerCard = currentPlayerPlayedCards.get(currentPlayerPlayedCards.size() - 1);
        Card opponentPlayerCard = opponentPlayerPlayedCards.get(opponentPlayerPlayedCards.size() - 1);

        //get the statValue of both cards and compare them
        int currentStatValue;
        int opponentStatValue;
        try {
            currentStatValue = getStatValue(currentPlayerCard, currentStatName);
            opponentStatValue = getStatValue(opponentPlayerCard, currentStatName);
        }
        catch (ResponseStatusException e) {
            throw e;
        }

        if (currentStatValue == opponentStatValue) {
            //idea: append new cards to playedCards from hand and return
            //player can choose a new stat to compare after a draw
            game.setRoundStatus(RoundStatus.DRAW);

            //get the next card from hand and append it to playedCards
            //do nothing if the player has no more cards left in the hand
            List<Card> currentPlayerHand = currentPlayer.getHand();
            if(!currentPlayerHand.isEmpty()){
                Card nextCard = currentPlayerHand.remove(0);
                currentPlayerPlayedCards.add(nextCard);
                currentPlayer.setHand(currentPlayerHand);
                currentPlayer.setPlayedCards(currentPlayerPlayedCards);
            }

            List<Card> opponentPlayerHand = opponentPlayer.getHand();
            if(!opponentPlayerHand.isEmpty()){
                Card nextCard = opponentPlayerHand.remove(0);
                opponentPlayerPlayedCards.add(nextCard);
                opponentPlayer.setHand(opponentPlayerHand);
                opponentPlayer.setPlayedCards(opponentPlayerPlayedCards);
            }

            game.setCurrentStatName(null);
        }

        if (currentStatValue > opponentStatValue) {
            //currentPlayer wins the round --> set roundStatus to WON
            game.setRoundStatus(RoundStatus.WON);

            //NEXT: take all cards in playedCards of both players, shuffle them and append to currentPLayerHand
            //take all cards from currentPlayedCards and append them to wonCards
            List<Card> wonCards = new ArrayList<>(currentPlayerPlayedCards);
            for(int i = 0; i < currentPlayerPlayedCards.size(); i++){
                currentPlayerPlayedCards.remove(0);
            }
            currentPlayer.setPlayedCards(new ArrayList<Card>());


            //take all cards from opponentPlayedCards and append them to wonCards
            wonCards.addAll(opponentPlayerPlayedCards);
            for(int i = 0; i < opponentPlayerPlayedCards.size(); i++){
                opponentPlayerPlayedCards.remove(0);
            }
            opponentPlayer.setPlayedCards(new ArrayList<Card>());

            //shuffle and append won Cards to currentPlayerHand
            Collections.shuffle(wonCards);
            List<Card> currentPlayerHand = currentPlayer.getHand();
            currentPlayerHand.addAll(wonCards);

            //update currentPlayerHand
            currentPlayer.setHand(currentPlayerHand);
            //check for  and update opponent
            game = checkForWinner(game);
            game.setOpponentPlayer(null);

        }
        if(currentStatValue < opponentStatValue) {
            //opponentPlayer wins the round --> set roundStatus to LOST
            game.setRoundStatus(RoundStatus.LOST);

            //NEXT: take all cards in playedCards of both players, shuffle them and append to currentPLayerHand
            //take all cards from currentPlayedCards and append them to wonCards
            List<Card> wonCards = new ArrayList<>(currentPlayerPlayedCards);
            for(int i = 0; i < currentPlayerPlayedCards.size(); i++){
                currentPlayerPlayedCards.remove(0);
            }
            currentPlayer.setPlayedCards(new ArrayList<Card>());

            //take all cards from opponentPlayedCards and append them to wonCards
            wonCards.addAll(opponentPlayerPlayedCards);
            for(int i = 0; i < opponentPlayerPlayedCards.size(); i++){
                opponentPlayerPlayedCards.remove(0);
            }
            opponentPlayer.setPlayedCards(new ArrayList<Card>());

            //shuffle and append won Cards to opponentPLayer
            Collections.shuffle(wonCards);
            List<Card> opponentPlayerHand = opponentPlayer.getHand();
            opponentPlayerHand.addAll(wonCards);


            //update currentPlayerHand
            opponentPlayer.setHand(opponentPlayerHand);

            //set opponent as current since he has won and check for winner
            game.setCurrentPlayer(opponentPlayer.getPlayerId());
            game.setOpponentPlayer(null);
            game = checkForWinner(game);
        }

        game = gameRepository.save(game);
        gameRepository.flush();
    }

    private int getStatValue(Card card, String currentStatName) throws ResponseStatusException{
        //always compare the stats of the last card in playedCards (when they have drawn the new card gets appended)
        for(int i = 0;i < card.getCardstats().size(); i++ ){
            String statName = card.getCardstats().get(i).getStatname();
            if(Objects.equals(currentStatName, statName)){
                return Integer.parseInt(card.getCardstats().get(i).getStatvalue());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no statName which equals the given one");
    }

    private Game checkForWinner(Game game){
        Session foundSession = sessionService.getSessionByGameCode(game.getGameCode().intValue());
        Deck deck = deckRepository.findByDeckId(foundSession.getDeckId());

        List<Card> cardList = deck.getCardList();
        List<Player> playerList = game.getPlayerList();

        //remove cards from deck until they can evenly be distributed inorder to compare to hand.size()
        while(cardList.size() % playerList.size() != 0){
            cardList = cardList.subList(0, cardList.size()- 1);
        }

        //iterate over all players and check for winner and inactive players
        for(Player player : playerList){
            if(player.getHand().size() == cardList.size()){
                game.setWinner(player.getPlayerId());
            }
            if(player.getHand().isEmpty()){
                player.setPlayerStatus(PlayerStatus.INACTIVE);
            }
        }
        return game;
    }

}



