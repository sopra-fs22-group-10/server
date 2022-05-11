package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.JoinSessionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.SessionService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Session Controller
 * This class is responsible for handling all REST request that are related to
 * the session.
 * The controller will receive the request and delegate the execution to the
 * SessionService and finally return the result.
 */
@RestController
public class SessionController {

    private final SessionService sessionService;
    private final UserService userService;

    SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }


    @GetMapping("/session/{gameCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SessionGetDTO getSessionByGameCode(@PathVariable int gameCode, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);

        Session session = sessionService.getSessionByGameCode(gameCode);
        return DTOMapper.INSTANCE.convertEntityToSessionGetDTO(session);
    }

    @PostMapping("/session/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SessionGetDTO createSession(@RequestBody SessionPostDTO sessionPostDTO, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);
        // convert API user to internal representation
        Session sessionInput = DTOMapper.INSTANCE.convertSessionPostDTOtoEntity(sessionPostDTO);

        // create session
        Session createdSession = sessionService.createSession(sessionInput);

        // convert internal representation of session back to API
        return DTOMapper.INSTANCE.convertEntityToSessionGetDTO(createdSession);
    }

    @DeleteMapping("/session/{gameCode}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByGameCode(@PathVariable int gameCode, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);
        sessionService.deleteSessionByGameCode(gameCode);
    }

    @PostMapping("/session/join/{gameCode}")
    @ResponseStatus(HttpStatus.OK)
    public SessionGetDTO joinSessionByGameCode(@PathVariable int gameCode, @RequestBody JoinSessionPostDTO joinSessionPostDTO, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);

        String username = joinSessionPostDTO.getUsername();

        Session joinedSession = sessionService.joinSessionByGameCode(gameCode, username);

        //convert internal representation of session back to API
        return DTOMapper.INSTANCE.convertEntityToSessionGetDTO(joinedSession);
    }

    @PutMapping("/session/{gameCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SessionGetDTO updateSessionByGameCode(@PathVariable int gameCode, @RequestBody SessionPostDTO sessionPostDTO, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);

        Session sessionToUpdate = DTOMapper.INSTANCE.convertSessionPostDTOtoEntity(sessionPostDTO);

        sessionToUpdate.setGameCode(gameCode);

        Session updatedSession = sessionService.updateSession(sessionToUpdate);

        return DTOMapper.INSTANCE.convertEntityToSessionGetDTO(updatedSession);
    }

    @PutMapping("/session/leave/{gameCode}")
    @ResponseStatus(HttpStatus.OK)
    public void leaveSession(@PathVariable int gameCode, @RequestHeader("Authentication") String auth) {
        User user = userService.getUserByAuthentication(auth);

        String username = user.getUsername();

        sessionService.leaveSession(gameCode, username);
    }
}
