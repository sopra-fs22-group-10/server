package ch.uzh.ifi.hase.soprafs22.service;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
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

    private final DeckService deckService;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository, DeckService deckService) {
        this.userRepository = userRepository;
        this.deckService = deckService;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }
    /*
    public void logout(Long userId, UserStatus newstatus){
        checkIfIDExists(userId);
        User user = getUserById(userId);
        user.setStatus(newstatus);
        userRepository.flush();
    }

     */
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


    //Later will include AccessCode to addPrivate Decks
    public void addDeck(Long deckId, Long userId){
        Deck deckToAdd = deckService.getDeckById(deckId);
        User user = getUserByID(userId);
        if(user.getDeckList().contains(deckToAdd)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can't add a existing deck twice");
        }

        List<Deck> deckList = user.getDeckList();
        if(user.getDeckList() == null){
            deckList = new ArrayList<>();
        }

        deckList.add(deckToAdd);
        user.setDeckList(deckList);
        userRepository.flush();

    }

    public User getUserByAuthentication(String Authentication){
        User user = userRepository.findByAuthentication(Authentication);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user associated with this Authentication");
        }
        return user;
    }

    public void removeDeck(Long deckId, Long userId){
        Deck deckToRemove = deckService.getDeckById(deckId);
        User user = getUserByID(userId);
        List<Deck> deckList = user.getDeckList();
        deckList.remove(deckToRemove);
        user.setDeckList(deckList);
        userRepository.flush();
    }

    public User getUserByID(Long Id) throws ResponseStatusException{
        Optional<User> foundUser = userRepository.findById(Id);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account for this userID was found!");
    }



    public User saveUser(User user) {
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        User returnUser = userRepository.save(user);
        userRepository.flush();
        return returnUser;
    }

    public void deleteUser(User user) {
        userRepository.deleteById(user.getUserId());
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
        User userById = getUserByID(userId);
        User user = getUserByAuthentication(auth);



        if (userById != user){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not authorized!");
        }

        user.setAuthentication(generateAuthToken());
        userRepository.flush();
    }


    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *

     *
     */


    public void checkIfUserExistsByAuthentication(String auth){
        if (userRepository.findByAuthentication(auth) == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "To acess this resource you have to be a logged in user with a valid Authentication!");
        }

    }

    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }
     /*
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

    private String generateAuthToken(){
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
