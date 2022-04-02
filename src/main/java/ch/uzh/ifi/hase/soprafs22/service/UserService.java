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
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



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



    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);

        checkIfUserExists(newUser);
        LocalDateTime now = LocalDateTime.now();
        newUser.setCreationDate(now);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }


    public void ChangeUserData(String username,String birthday, String token, Long id)  {

        User user1 = getUserById(id);

        verifyTokenandMatchId(token, id);

        if(birthday != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            try {


                LocalDate date = LocalDate.parse(birthday, formatter);


            }
            catch (DateTimeParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The date Format wasn't right");
            }

            LocalDate date = LocalDate.parse(birthday, formatter);
            user1.setBirthdate(date);
        }
        if(username != null) {
            user1.setUsername(username);
        }



        userRepository.flush();

        log.debug("Updated Information for User: {}", user1);

    }

    public User getUserById(Long id){

        checkIfIDExists(id);
        Optional<User> potentialuser = userRepository.findById(id);

        User user = potentialuser.get();

        return user;
    }

    public User getUserByUsername(User temoraryuser){
        //checkIfIDExists(id);

        User potentialuser = userRepository.findByUsername(temoraryuser.getUsername());

        return potentialuser;
    }

    public void logout(Long userId, UserStatus newstatus){
        checkIfIDExists(userId);
        User user = getUserById(userId);
        user.setStatus(newstatus);
        userRepository.flush();
    }
    //After checking if username and password are correct the Token of the corresponding user is returned and The status is set to Online
    //Checks user credentials, set Status online and return actual user
    public User loginUsers(String username, String password){
        User ActualUser = userRepository.findByUsername(username);

        checkIfUsernameAndPasswordMatch(ActualUser, password);

        ActualUser.setStatus(UserStatus.ONLINE);

        return ActualUser;

    }
    public void verifyTokenandMatchId(String token, Long id){
        User userToCheck = userRepository.findByToken(token);
        if (userToCheck == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have access to this resource, because you are not logged in");
        }
        if (userToCheck.getId() != id) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have access to this recourse, you can't change userprofile of other users");
        }
    }

    private void checkIfUsernameAndPasswordMatch(User ActualUser, String password){

        String baseErrorMessage = "The %s you entered, doesn't match the %s. Please try again!";
        if (ActualUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry, there doesn't exist a user with the username you entered");
        }else if(! ActualUser.getPassword().equals(password)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "password", "username"));
        }
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
    private void checkIfIDExists(Long ID) {

        String baseErrorMessage = "The %s provided does not exist. There %s not any user assotiated with this Id";
        if (! userRepository.existsById(ID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "ID", "is"));
        }
    }
}
