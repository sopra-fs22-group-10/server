package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
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
import java.util.*;

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

    private final UserRepository userRepository;

    private final DeckRepository deckRepository;

    @Autowired
    public SessionService(@Qualifier("sessionRepository") SessionRepository sessionRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("deckRepository") DeckRepository deckRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
    }

    public List<Session> getSessions() {
        return this.sessionRepository.findAll();
    }

    public Session createSession(Session newSession) throws ResponseStatusException {

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newSession = sessionRepository.save(newSession);
        sessionRepository.flush();

        newSession.setGameCode(generateGameCode(newSession.getSessionId()));
        List<String> userList = new ArrayList<>();
        newSession.setUserList(userList);
        try{
            checkSessionCreationInput(newSession);
        }catch (ResponseStatusException e) {throw e; }

        newSession.addUser(newSession.getHostUsername());

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

   public void checkSessionCreationInput(Session newSession) throws ResponseStatusException{
      //check if the user exists
       User userToCheck = userRepository.findByUsername(newSession.getHostUsername());

       if(userToCheck == null){ throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given User does not exist");}
       //check if User is authorized
       //TO BE IMPLEMENTED

       //check if MaxPlayer Input is correct
       if(newSession.getMaxPlayers() > 6){
       throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The maximum number of Players is 6!");
       }
       //set maxPlayers to a minimum value of 2
       if(newSession.getMaxPlayers() < 2) {newSession.setMaxPlayers(2); }

       //check if Deck exists
       Deck deckToCheck = deckRepository.findByDeckId(newSession.getDeckId());
       if(deckToCheck == null) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given Deck does not exist"); }
  }
}
