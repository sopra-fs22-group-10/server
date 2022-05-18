package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.RoundGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RestController
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    GameController(GameService gameService, UserService userService ){

        this.gameService = gameService;
        this.userService = userService;

    }


    @GetMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameByGameCode(@PathVariable Long gameCode, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);

        // fetch game in the internal representation
        Game game = gameService.findGameByGameCode(gameCode);

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @GetMapping("/session/{gameCode}/round")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundGetDTO getRoundByGameCode(@PathVariable Long gameCode, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);

        // fetch game in the internal representation
        Game game = gameService.findGameByGameCode(gameCode);

        //Authentification Check

        return DTOMapper.INSTANCE.convertEntityToRoundGetDTO(game);
    }

    @PostMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@PathVariable Long gameCode, @RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);

        // fetch game in the internal representation
        Game game = gameService.createGame(gameCode);

        //Authentification Check

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PutMapping("/session/{gameCode}/round")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundGetDTO getGameByGameCode(@PathVariable Long gameCode, @RequestBody GamePutDTO gamePutDTO,@RequestHeader("Authentication") String auth) {
        userService.checkIfUserExistsByAuthentication(auth);

        // fetch game in the internal representation
       Game game = gameService.gameUpdate(gameCode, gamePutDTO.getOpponentPlayer(), gamePutDTO.getCurrentStatName());

        //Authentification Check

        return DTOMapper.INSTANCE.convertEntityToRoundGetDTO(game);
    }

    @DeleteMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGameByGameCode(@PathVariable Long gameCode, @RequestHeader("Authentication") String auth){
        User foundUser = userService.getUserByAuthentication(auth);

        gameService.deleteGameByGameCode(gameCode);
    }
}
