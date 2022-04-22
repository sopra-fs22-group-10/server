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
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional(propagation=Propagation.REQUIRED)
    public Session joinSessionByGameCode(int gameCode, String username) throws ResponseStatusException{
        User user;
        Session sessionToJoin;
        //check if user exists
        try{
            user = checkIfUserExists(username);
        } catch(ResponseStatusException e) {throw e; }

        //check if Session exists
        try{
            sessionToJoin = checkIfSessionExists(gameCode);
        } catch(ResponseStatusException e) {throw e; }

        //check if there are more Players allowed
        try{
            checkIfSessionIsFull(sessionToJoin);
        } catch(ResponseStatusException e) {throw e; }

        sessionToJoin.addUser(username);
        sessionRepository.save(sessionToJoin);
        sessionRepository.flush();


        return sessionToJoin;

    }

    @Transactional(propagation= Propagation.REQUIRED)
    public Session updateSession(Session sessionInput) throws ResponseStatusException {
        Session sessionToUpdate;
        try{
            sessionToUpdate = getSessionByGameCode(sessionInput.getGameCode());
        } catch (ResponseStatusException e) {throw e; }

        try{
            checkSessionCreationInput(sessionInput);
        }catch (ResponseStatusException e) {throw e; }

        sessionToUpdate.setDeckId(sessionInput.getDeckId());
        sessionToUpdate.setMaxPlayers(sessionInput.getMaxPlayers());
        sessionToUpdate.setHostUsername(sessionInput.getHostUsername());
        sessionRepository.save(sessionToUpdate);
        sessionRepository.flush();
        return sessionToUpdate;
    }

    public Session getSessionByGameCode(int gameCode) throws ResponseStatusException{
        Session foundSession = sessionRepository.findByGameCode(gameCode);
        if(foundSession != null){
            return foundSession;
        }
        throw new ResponseStatusException(HttpStatus.resolve(404), "There exists no Session with given gamecode");
    }





  //internal helper Methods
  private void checkSessionCreationInput(Session newSession) throws ResponseStatusException{
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
    private User checkIfUserExists(String username) throws ResponseStatusException {
        User foundUser = userRepository.findByUsername(username);

        if(foundUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There exists no User with given username");
        }

        return foundUser;
    }
    private Session checkIfSessionExists(int gameCode) throws ResponseStatusException{
        Session foundSession =  sessionRepository.findByGameCode(gameCode);

        if(foundSession == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There exists no Session with given gameCode");
        }
        return foundSession;

    }

    private void checkIfSessionIsFull(Session session){
        int maxPlayers = session.getMaxPlayers();
        int current = session.getUserList().size();

        if(current == maxPlayers) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Session is already full");
        }
    }
}

