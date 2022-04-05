package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SessionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.SessionService;
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
public class SessionController {

    private final SessionService sessionService;

    SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/session/create")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SessionGetDTO createSession(@RequestBody SessionPostDTO sessionPostDTO, HttpServletResponse response) {
        response.addHeader("Accept", "application/json"); //tell accepted return type in header

        Session sessionInput = DTOMapper.INSTANCE.convertSessionPostDTOtoEntity(sessionPostDTO);
        Session createdSession;

        try {
            createdSession = sessionService.createSession(sessionInput);
        }
        catch (ResponseStatusException e) { throw e;}


        return DTOMapper.INSTANCE.convertEntityToSessionGetDTO(createdSession);
    }





}
