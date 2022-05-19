package ch.uzh.ifi.hase.soprafs22.rest.mapper;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
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
}
