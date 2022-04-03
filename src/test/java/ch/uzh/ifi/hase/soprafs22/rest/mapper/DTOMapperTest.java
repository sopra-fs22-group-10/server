package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
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
        user.setId(1L);

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }
}
