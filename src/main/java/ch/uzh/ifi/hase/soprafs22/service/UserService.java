package ch.uzh.ifi.hase.soprafs22.service;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.util.Base64;

import java.util.List;
import java.util.Optional;
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

    public void logout(Long userId, UserStatus newstatus){
        checkIfIDExists(userId);
        User user = getUserById(userId);
        user.setStatus(newstatus);
        userRepository.flush();
    }
    //After checking if username and password are correct the Token of the corresponding user is returned and The status is set to Online
    //Checks user credentials, set Status online and return actual user
    /*
    public void verifyTokenandMatchId(String token, Long id){
        User userToCheck = userRepository.findByToken(token);
        if (userToCheck == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have access to this resource, because you are not logged in");
        }
        if (userToCheck.getId() != id) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have access to this recourse, you can't change userprofile of other users");
        }
    }
    */



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
        String toEncode = newUser.getUsername() + ":" + newUser.getPassword();
        String authentication = Base64.getEncoder().encodeToString(toEncode.getBytes());
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
        log.debug("Logged into account for User: {}", userInput);
        return userRepository.findByUsername(userInput.getUsername());
    }


    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *

     *
     */

    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }
 
     * @param userToBeCreated: the user that is tried to be created
     * @throws org.springframework.web.server.ResponseStatusException: Exception that tells us user already exists
     * @see User
     */
   

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
}
