package ch.uzh.ifi.hase.soprafs22.repository;


import ch.uzh.ifi.hase.soprafs22.entity.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class SessionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void findByGameCode() {
        // given
        Session session = new Session();
        session.setMaxPlayers(1);
        session.setDeckId(1L);
        session.setGameCode(1);
        session.setHostUsername("username");
        session.setUserList(new ArrayList<String>());
        session.addUser("username");

        entityManager.persist(session);
        entityManager.flush();

        // when
        Session found = sessionRepository.findBySessionId(session.getSessionId());

        // then
        assertNotNull(found.getSessionId());
        assertEquals(found.getHostUsername(), session.getHostUsername());
        assertEquals(found.getGameCode(), session.getGameCode());
        assertEquals(found.getDeckId(), session.getDeckId());
        assertEquals(found.getMaxPlayers(), session.getMaxPlayers());
        assertEquals(found.getUserList(), session.getUserList());
    }
}