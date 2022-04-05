package ch.uzh.ifi.hase.soprafs22.service;


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
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Random;

@Service
@Transactional
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    @Autowired
    public SessionService(@Qualifier("sessionRepository") SessionRepository sessionRepository, @Qualifier("userRepository") UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }


    public Session createSession(Session newSession) throws ResponseStatusException {

        String authentication = generateSessionId(newSession.getSessionId().intValue());
        newSession.setAuthentication(authentication);
        int newSessionId = Integer.parseInt(authentication);
        newSession.setGameCode(newSessionId);

        try{
            checkSessionDetails(newSession);
        }
        catch (ResponseStatusException e) { throw e; }

        newSession = saveSession(newSession);
        log.debug("Created Information for Session: {}", newSession);
        return newSession;
    }

    public Session saveSession(Session session) {
        Session returnSession = sessionRepository.save(session);
        sessionRepository.flush();
        return returnSession;
    }
    //We can add more Checking Points to this method
    private void checkSessionDetails(Session createdSession) throws ResponseStatusException {

        User userByUsername = userRepository.findByUsername(createdSession.getUsername());

        if(userByUsername == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Host of the Session is no User!");
        }
    }

    private String generateSessionId(int sessionId) {
        StringBuilder idToString = new StringBuilder(String.valueOf(sessionId));

        Random random = new Random();

        while(idToString.length() < 6){
            int randomInt = random.nextInt(9);
            idToString.append(randomInt);
        }

        return idToString.toString();
    }
}
