package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findById_success() {
        // given
        User newUser = new User();
        newUser.setUsername("testUsername");
        newUser.setPassword("testPassword");
        newUser.setAuthentication("testAuthentication");
        newUser.setStatus(UserStatus.OFFLINE);

        entityManager.persist(newUser);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(newUser.getUsername());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), newUser.getUsername());
        assertEquals(found.getStatus(), newUser.getStatus());
        assertEquals(found.getPassword(), newUser.getPassword());
        assertEquals(found.getAuthentication(), newUser.getAuthentication());
    }
}
