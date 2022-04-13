package ch.uzh.ifi.hase.soprafs22.rest.mapper;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginDTO;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

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
        sessionPostDTO.setUsername("username");
        sessionPostDTO.setMaxPlayers(1);
        sessionPostDTO.setDeckId(1L);

        //MAP create session Entity from postDTO
        Session session = DTOMapper.INSTANCE.convertSessionPostDTOtoEntity(sessionPostDTO);

        //check
        assertEquals(sessionPostDTO.getUsername(), session.getUsername());
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
        session.setUsername("username");

        //MAP create SessionGetDTO
        SessionGetDTO sessionGetDTO = DTOMapper.INSTANCE.convertEntityToSessionGetDTO(session);

        //check
        assertEquals(session.getSessionId(), sessionGetDTO.getSessionId());
        assertEquals(session.getGameCode(), sessionGetDTO.getGameCode());
    }
}
