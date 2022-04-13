package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class    SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(@Qualifier("sessionRepository") SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getSessions() {
        return this.sessionRepository.findAll();
    }

    public Session createSession(Session newSession) {

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newSession = sessionRepository.save(newSession);
        sessionRepository.flush();

        newSession.setGameCode(generateGameCode(newSession.getSessionId()));

        log.debug("Created Information for User: {}", newSession);

        return newSession;
    }

    public void deleteSessionById(Long sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public void deleteSessionByGameCode(int gameCode) {
        sessionRepository.deleteByGameCode(gameCode);
    }

    public Session getSessionById(Long sessionId) {
        Optional<Session> foundSession = sessionRepository.findById(sessionId);
        if (foundSession.isPresent()) {
            return foundSession.get();
        }
        throw new ResponseStatusException(HttpStatus.resolve(404), "No session for this sessionId found");
    }

    public Session getSessionByGameCode(int gameCode) throws ResponseStatusException{
        Session foundSession = sessionRepository.findByGameCode(gameCode);
        if(foundSession != null){
            return foundSession;
        }
        throw new ResponseStatusException(HttpStatus.resolve(404), "No session for this GameCode found");



    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * //@param ToBeCreated
     * @throws ResponseStatusException
     * @see Session
     */

  /*
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
      throw new ResponseStatusException(HttpStatus.resolve(409),
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.resolve(409), String.format(baseErrorMessage, "username", "is"));
    } else if (userByName != null) {
      throw new ResponseStatusException(HttpStatus.resolve(409), String.format(baseErrorMessage, "name", "is"));
    }
  }*/

    private int generateGameCode(Long sessionId) {
        String stringValue = sessionId.toString();

        Random random = new Random();

        while(stringValue.length() < 6) {

            int randomNumber = random.nextInt(10);
            stringValue = stringValue + String.valueOf(randomNumber);

        }

        int gameCode = Integer.parseInt(stringValue);
        return gameCode;
    }
}
