package ch.uzh.ifi.hase.soprafs22.rest.mapper;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    /*
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("password");
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        */

    public void testCreateUser_fromUserLoginDTO_toUser_success() {
        // create UserLoginDTO
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setPassword("testPassword");
        userLoginDTO.setUsername("testUsername");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserLoginDTOtoEntity(userLoginDTO);

        // check content
        assertEquals(userLoginDTO.getPassword(), user.getPassword());
        assertEquals(userLoginDTO.getUsername(), user.getUsername());

    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();

        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setAuthentication("testAuthentication");
        user.setStatus(UserStatus.OFFLINE);
        user.setUserId(1L);

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getUserId(), userGetDTO.getUserId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void createSessionEntitySuccess(){
        //create PostDTO
        SessionPostDTO sessionPostDTO = new SessionPostDTO();
        sessionPostDTO.setHostUsername("username");
        sessionPostDTO.setHostId(1L);
        sessionPostDTO.setMaxPlayers(1);
        sessionPostDTO.setDeckId(1L);

        //MAP create session Entity from postDTO
        Session session = DTOMapper.INSTANCE.convertSessionPostDTOtoEntity(sessionPostDTO);

        //check
        assertEquals(sessionPostDTO.getHostUsername(), session.getHostUsername());
        assertEquals(sessionPostDTO.getHostId(), session.getHostId());
        assertEquals(sessionPostDTO.getDeckId(), session.getDeckId());
        assertEquals(sessionPostDTO.getMaxPlayers(), session.getMaxPlayers());
    }

    @Test
    public void createGetSessionDTOSuccess(){
        //create new session
        Session session = new Session();
        session.setSessionId(1L);
        session.setMaxPlayers(1);
        session.setDeckId(1L);
        session.setGameCode(1);
        session.setHostUsername("username");
        session.setHostId(1L);

        //MAP create SessionGetDTO
        SessionGetDTO sessionGetDTO = DTOMapper.INSTANCE.convertEntityToSessionGetDTO(session);

        //check
        assertEquals(session.getGameCode(), sessionGetDTO.getGameCode());
        assertEquals(session.getHostUsername(), sessionGetDTO.getHostUsername());
        assertEquals(session.getHostId(), sessionGetDTO.getHostId());
        assertEquals(session.getUserList(), sessionGetDTO.getUserList());
        assertEquals(session.getMaxPlayers(), sessionGetDTO.getMaxPlayers());
        assertEquals(session.getDeckId(), sessionGetDTO.getDeckId());
    }

    @Test
    public void createGetGameDTOSuccess() {
        Game game = new Game();
        game.setGameCode(1L);
        game.setWinner(2L);
        game.setCurrentPlayer(2L);
        game.setOpponentPlayer(3L);
        game.setPlayerList(new ArrayList<Player>());

        //create GameGetDTO
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        assertEquals(game.getWinner(), gameGetDTO.getWinner());
        assertEquals(game.getCurrentPlayer(), gameGetDTO.getCurrentPlayer());
        assertEquals(game.getOpponentPlayer(), gameGetDTO.getOpponentPlayer());
        assertEquals(game.getPlayerList(), gameGetDTO.getPlayerList());

    }

    @Test
    public void createCardPutDTOtoEntitySuccess() {
       
       CardPutDTO cardPutDTO = new CardPutDTO();
       cardPutDTO.setCardId(22L);
       cardPutDTO.setCardname("NewCardname");
       List<Stat> statList = new ArrayList<>();
       Stat somestat = new Stat();
       statList.add(somestat);
       cardPutDTO.setCardstats(statList);
       cardPutDTO.setImage("Newimage");

        //create GameGetDTO
        
        Card card = DTOMapper.INSTANCE.convertCardPutDTOtoEntity(cardPutDTO);

        assertEquals(cardPutDTO.getCardId(), card.getCardId());
        assertEquals(cardPutDTO.getCardname(), card.getCardname());
        assertEquals(cardPutDTO.getImage(), card.getImage());
        assertEquals(cardPutDTO.getCardstats(), card.getCardstats());

    }

    @Test
    public void createCardPostDTOtoEntitySuccess() {

        CardPostDTO cardPostDTO = new CardPostDTO();
        cardPostDTO.setCardname("NewCardname");
        List<Stat> statList = new ArrayList<>();
        Stat somestat = new Stat();
        statList.add(somestat);
        cardPostDTO.setCardstats(statList);
        cardPostDTO.setImage("Newimage");

        //create GameGetDTO

        Card card = DTOMapper.INSTANCE.convertCardPostDTOtoEntity(cardPostDTO);

     
        assertEquals(cardPostDTO.getCardname(), card.getCardname());
        assertEquals(cardPostDTO.getImage(), card.getImage());
        assertEquals(cardPostDTO.getCardstats(), card.getCardstats());

    }

    @Test
    public void createDeckPutDTOtoEntitySuccess() {

        DeckPutDTO deckPutDTO = new DeckPutDTO();
        deckPutDTO.setDeckImage("NewDeckimage");
        deckPutDTO.setDeckname("NewDeckname");

        //create GameGetDTO

        Deck deck = DTOMapper.INSTANCE.convertDeckPutDTOtoEntity(deckPutDTO);


        assertEquals(deckPutDTO.getDeckname(), deck.getDeckname());
        assertEquals(deckPutDTO.getDeckImage(), deck.getDeckImage());
        
    }

    @Test
    public void createDeckPostDTOtoEntitySuccess() {

        DeckPostDTO deckPostDTO = new DeckPostDTO();
        deckPostDTO.setDeckname("NewDeckname");

        //create GameGetDTO

        Deck deck = DTOMapper.INSTANCE.convertDeckPostDTOtoEntity(deckPostDTO);


        assertEquals(deckPostDTO.getDeckname(), deck.getDeckname());

    }
    
}
