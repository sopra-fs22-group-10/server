package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class GameController {

    private final DeckService deckService;
    private final TemplateService templateService;
    private final CardService cardService;
    private final UserService userService;
    private final GameService gameService;
    private final DeckRepository deckRepository;
    //private EntityManager entityManager;
    /*
    private final UserRepository userRepository;

    private final CardRepository cardRepository;
    private final StatRepository statRepository;
    private final TemplateRepository templateRepository;

     */



    private EntityManager entityManager;


    @Qualifier(value = "entityManager")
    private EntityManager createentityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }



    GameController(DeckService deckService, DeckRepository deckRepository, TemplateService templateService, CardService cardService, UserService userService, GameService gameService, EntityManager entityManager){//, UserRepository userRepository, CardRepository cardRepository, TemplateRepository templateRepository, StatRepository statRepository) {
        this.deckService = deckService;
        this.templateService = templateService;
        this.cardService = cardService;
        this.userService = userService;
        this.gameService = gameService;

        this.deckRepository = deckRepository;
        /*
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
        this.cardRepository = cardRepository;
        this.statRepository = statRepository;

         */

        this.entityManager = entityManager;
    }


    @GetMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameByGameCode(@PathVariable Long gameCode) {
        // fetch game in the internal representation
        Game game = gameService.findGameByGameCode(gameCode);

        //Authentification Check

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @GetMapping("/session/{gameCode}/round")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public RoundGetDTO getRoundByGameCode(@PathVariable Long gameCode) {
        // fetch game in the internal representation
        Game game = gameService.findGameByGameCode(gameCode);

        //Authentification Check

        return DTOMapper.INSTANCE.convertEntityToRoundGetDTO(game);
    }

    @PostMapping("/session/{gameCode}/game")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public GameGetDTO createGame(@PathVariable Long gameCode) {
        // fetch game in the internal representation
        Game game = gameService.createGame(gameCode);

        //Authentification Check

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PutMapping("/session/{gameCode}/round")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    //@ResponseBody
    public RoundGetDTO getGameByGameCode(@PathVariable Long gameCode, @RequestBody GamePutDTO gamePutDTO) {
        // fetch game in the internal representation
       Game game = gameService.gameUpdate(gameCode, gamePutDTO.getOpponentPlayer(), gamePutDTO.getCurrentStatName()); //should be GamePutDTO

        //Authentification Check

        return DTOMapper.INSTANCE.convertEntityToRoundGetDTO(game);
    }
}
