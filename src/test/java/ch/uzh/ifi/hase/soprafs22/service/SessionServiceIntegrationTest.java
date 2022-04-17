package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.SessionRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test class for the SessionResource REST resource.
 *
 * @see SessionService
 */
@WebAppConfiguration
@SpringBootTest
public class SessionServiceIntegrationTest {

    @Qualifier("sessionRepository")
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    @BeforeEach
    public void setup() {
        sessionRepository.deleteAll();
    }

    @Test
    public void createSessionSuccess() {
        // given
        assertNull(sessionRepository.findBySessionId(1L));

        Session session = new Session();
        session.setMaxPlayers(1);
        session.setDeckId(1L);
        session.setGameCode(1);
        session.setUsername("username");

        // when
        Session createdSession = sessionService.createSession(session);

        // then
        assertEquals(session.getSessionId(), createdSession.getSessionId());
        assertEquals(session.getUsername(), createdSession.getUsername());
        assertEquals(session.getGameCode(), createdSession.getGameCode());
        assertEquals(session.getMaxPlayers(), createdSession.getMaxPlayers());
        assertEquals(session.getDeckId(), createdSession.getDeckId());
    }
}