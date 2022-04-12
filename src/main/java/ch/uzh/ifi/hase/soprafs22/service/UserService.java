package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUserByID(Long Id) throws ResponseStatusException{
        Optional<User> foundUser = userRepository.findById(Id);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        throw new ResponseStatusException(HttpStatus.resolve(404), "No account for this userID was found!");
    }

    public User saveUser(User user) {
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        User returnUser = userRepository.save(user);
        userRepository.flush();
        return returnUser;
    }

    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }

    public User createUser(User newUser) throws ResponseStatusException {
        String authentication = generateAuthToken();
        newUser.setAuthentication(authentication);
        newUser.setStatus(UserStatus.OFFLINE);

        try {
            checkIfUserExists(newUser);
        } catch (ResponseStatusException e){throw e;}
        newUser = saveUser(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User accessUser(User userInput) throws ResponseStatusException {
        try {
            checkLoginCredentials(userInput);
        } catch (ResponseStatusException e){throw e;}
        User loggedInUser = userRepository.findByUsername(userInput.getUsername());
        log.debug("Logged into account for User: {}", loggedInUser);
        return loggedInUser;
    }

    public void logoutUser(Long userId, String auth){
        Optional <User> loggedOutUser = userRepository.findById(userId);
        if (loggedOutUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.resolve(404), "No account for this userID was found!");
        } else {
            if (!auth.equals(loggedOutUser.get().getAuthentication())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not authorized!");
            }
        }
        loggedOutUser.get().setAuthentication(generateAuthToken());
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated: the user that is tried to be created
     * @throws org.springframework.web.server.ResponseStatusException: Exception that tells us user already exists
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) throws ResponseStatusException {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.resolve(409), String.format(baseErrorMessage, "username", "is"));
        }
    }

    /**
     * This is a helper method that will check the login credentials. The method will do nothing
     *  if the credentials are correct and throw an error otherwise.
     *
     * @param userToAccess: the user whose profile is tried to log into
     * @throws org.springframework.web.server.ResponseStatusException: exception to show that login was incorrect
     * @see User
     */
    private void checkLoginCredentials(User userToAccess) throws ResponseStatusException {
        User userByUsername = userRepository.findByUsername(userToAccess.getUsername());

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No account for this username was found!");
        }
        else if (!userByUsername.getPassword().equals(userToAccess.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Wrong password!");
        }
    }

    private String generateAuthToken(){
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
