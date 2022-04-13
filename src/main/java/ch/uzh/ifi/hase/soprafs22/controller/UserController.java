package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {

        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @GetMapping("/users/{userId}")
    @ResponseBody
    public UserGetDTO getUserById(@PathVariable Long userId) {

        User foundUser = userService.getUserByID(userId);
        /*try{
            foundUser = userService.getUserByID(userId);
        } catch (ResponseStatusException e){throw e;}*/

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }

    @PostMapping("/users")
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserLoginDTO userLoginDTO) {

        User userInput = DTOMapper.INSTANCE.convertUserLoginDTOtoEntity(userLoginDTO);
        User createdUser = userService.createUser(userInput);
        /*try{
            createdUser = userService.createUser(userInput);}
        catch (ResponseStatusException e){throw e;}*/

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserAccess(@RequestBody UserLoginDTO userLoginDTO) {

        User userInput = DTOMapper.INSTANCE.convertUserLoginDTOtoEntity(userLoginDTO);
        User accessedUser = userService.accessUser(userInput);
        /*try {
            accessedUser = userService.accessUser(userInput);
        } catch (ResponseStatusException e){throw e;}*/

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(accessedUser);
    }

    @PostMapping("/users/{userId}/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logoutUser(@PathVariable Long userId, @RequestHeader("Authentication") String auth) {

        try {
            userService.logoutUser(userId, auth);
        } catch (ResponseStatusException e){throw e;}
    }

    @PutMapping("/users/{userId}")
    @ResponseBody
    public void updateUserById(@PathVariable Long userId,
                                     @RequestBody UserPostDTO userPostDTO,
                                     @RequestHeader("Authentication") String auth) {

        User userToUpdate = userService.getUserByID(userId);
        /*try{
            userToUpdate = userService.getUserByID(userId);
        } catch(ResponseStatusException e){throw e;}*/

        if (!auth.equals(userToUpdate.getAuthentication())){
            throw new ResponseStatusException(HttpStatus.resolve(401), "Not authorized");}

        userToUpdate.setUsername(userPostDTO.getUsername());
        userService.saveUser(userToUpdate);
    }
}
