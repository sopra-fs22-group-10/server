package ch.uzh.ifi.hase.soprafs22.controller;


import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) { this.gameService = gameService; }

    @PostMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGameByGameCode(@PathVariable Long gameCode) {
        //create game
        Game createdGame = gameService.createGame(gameCode);

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @GetMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public GameGetDTO getGameByGameCode(@PathVariable Long gameCode){
        //get game to return
        Game game = gameService.findGameByGameCode(gameCode);

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PutMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updateGameByGameCode(GamePostDTO gamePostDTO){
        return;
    }

    @DeleteMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteGameByGameCode(){
        return;
    }
}
