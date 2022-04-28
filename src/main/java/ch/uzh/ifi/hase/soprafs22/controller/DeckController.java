package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.CardService;
import ch.uzh.ifi.hase.soprafs22.service.DeckService;
import ch.uzh.ifi.hase.soprafs22.service.TemplateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
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
public class DeckController {

    private final DeckService deckService;
    private final TemplateService templateService;
    private final CardService cardService;
    private final UserService userService;
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



    DeckController(DeckService deckService, DeckRepository deckRepository, TemplateService templateService, CardService cardService, UserService userService, EntityManager entityManager){//, UserRepository userRepository, CardRepository cardRepository, TemplateRepository templateRepository, StatRepository statRepository) {
        this.deckService = deckService;
        this.templateService = templateService;
        this.cardService = cardService;
        this.userService = userService;

        this.deckRepository = deckRepository;
        /*
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
        this.cardRepository = cardRepository;
        this.statRepository = statRepository;

         */

        this.entityManager = entityManager;
    }


    @GetMapping("/decks")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Deck> getAllDecks(@RequestHeader("Authentication") String auth) {
        // fetch all decks in the internal representation
        userService.checkIfUserExistsByAuthentication(auth);
        List<Deck> decks = deckService.getPublicDecks();

        //Authentification Check

        return decks;
    }

    @GetMapping("/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Deck getDeckById(@PathVariable Long deckId, @RequestHeader("Authentication") String auth) {
        // fetch all decks in the internal representation
        userService.checkIfUserExistsByAuthentication(auth);
        Deck deck = deckService.getDeckById(deckId);
        if(deck.getDeckstatus()==DeckStatus.PRIVATE){
            User user = userService.getUserByAuthentication(auth);
            if(!user.getDeckList().contains(deck)){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User has no access to this resource");
            }

        }


        return deck;
    }
    @GetMapping("/decks/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Deck> getUserDecks(@PathVariable Long userId, @RequestHeader("Authentication") String auth) {
        // fetch all decks in the internal representation
        User user = userService.getUserByID(userId);
        if(user != userService.getUserByAuthentication(auth)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User has no access to this resource");
        }
        if(user.getDeckList() == null){
            return new ArrayList<Deck>();
        }
        return user.getDeckList();

    }


    //Needs rework, since it doesn't work properly
    @DeleteMapping("/decks/{deckId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteDeck(@PathVariable Long deckId, @RequestHeader("Authentication") String auth){
        Deck deckToDelete = deckService.getDeckById(deckId);
        for(User user: userService.getUsers()){
            if(user.getDeckList().contains(deckToDelete)){
                if(!Objects.equals(user.getAuthentication(), auth)){
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User by Authentication can't alter/acess this resource !");
                }
                userService.removeDeck(deckId, user.getUserId());
            }
        }

        deckRepository.deleteById(deckId);
    }


    @PostMapping("/decks/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck createDeck(@PathVariable Long userId, @RequestBody DeckPostDTO deckPostDTO, @RequestHeader("Authentication") String auth){

        if(userService.getUserByID(userId) != userService.getUserByAuthentication(auth)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User by Authentication can't alter/acess this resource !");
        }
        Deck newDeck = DTOMapper.INSTANCE.convertDeckPostDTOtoEntity(deckPostDTO);

        //In create Deck the userId has to be added to link decks with user accounts
        Deck createdDeck = deckService.createDeck(newDeck);
        userService.addDeck(createdDeck.getDeckId(), userId);

        return createdDeck;
    }
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        response.addHeader("Accept", "application/json"); //tell accepted return type in header

        User userInput = DTOMapper.INSTANCE.convertUserLoginDTOtoEntity(userLoginDTO);
        User createdUser = userService.createUser(userInput);

        //Deck defaultDeck = deckService.createDefaultDeck();

        response.addHeader("Access-Control-Expose-Headers", "Authentication");
        response.addHeader("Authentication", createdUser.getAuthentication());

        //Adds TemplateDeck to every user created
        DeckPutDTO deckPutDTO = new DeckPutDTO();
        deckPutDTO.setDeckId(67L);
        AddExistingDeckToUser(createdUser.getUserId(), deckPutDTO, createdUser.getAuthentication());

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }


    @PutMapping("/decks/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void AddExistingDeckToUser(@PathVariable Long userId, @RequestBody DeckPutDTO deckPutDTO, @RequestHeader("Authentication") String auth){
        //Check if user exists
        User user = userService.getUserByID(userId);

        if(user != userService.getUserByAuthentication(auth)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User by Authentication can't alter/acess this resource !");
        }

        Long deckId = deckPutDTO.getDeckId();
        Deck existingDeck = deckService.getDeckById(deckId);
        if(existingDeck.getDeckstatus() == DeckStatus.PRIVATE){
            if(deckPutDTO.getDeckAccessCode() != null && !Objects.equals(deckPutDTO.getDeckAccessCode(), existingDeck.getDeckacesscode())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The provided Deck Access Code is wrong.");
            }
        }

        Template existingTemplate = existingDeck.getTemplate();
        List <Card> cardList = existingDeck.getCardList();


        for(Stat stat: existingDeck.getTemplate().getTemplatestats()){
            entityManager.detach(stat);
            stat.setStatId(null);
        }
        entityManager.detach(existingTemplate);
        List <Card> newCardList = new ArrayList<>();

        for(Card card : cardList){
            for(Stat stat: card.getCardstats()){
                entityManager.detach(stat);
                stat.setStatId(null);

            }
            entityManager.detach(card);
            card.setCardId(null);
            newCardList.add(cardService.createCard(card, existingTemplate));
        }

        entityManager.detach(existingDeck);
        existingDeck.getTemplate().setTemplateId(null);
        existingDeck.setTemplate(templateService.createTemplate(existingTemplate));
        existingDeck.setCardList(newCardList);
        existingDeck.setDeckId(null);
        existingDeck.setDeckname(null);
        existingDeck = deckService.createDeck(existingDeck);

       userService.addDeck(existingDeck.getDeckId(), userId);
    }


    @PostMapping("/decks/{deckId}/templates")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck CreateTemplate(@PathVariable Long deckId, @RequestBody TemplatePostDTO templatePostDTO, HttpServletResponse responseheader , @RequestHeader("Authentication") String auth){
        responseheader.setHeader("Accept", "application/jason");
        Template userTemplate = DTOMapper.INSTANCE.convertTemplatePostDTOtoEntity(templatePostDTO);
        User user = userService.getUserByAuthentication(auth);
        if(!user.getDeckList().contains(deckService.getDeckById(deckId))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User by Authentication can't alter/acess this resource !");
        }
        Template deckTemplate = templateService.createTemplate(userTemplate);

        //The template doesn't have to be transformed to a TemplateGetDTO since no info must be left out
        return deckService.setTemplate(deckTemplate, deckId);
    }

    @PostMapping("/decks/{deckId}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck CreateCard(@PathVariable Long deckId, @RequestBody CardPostDTO cardPostDTO, HttpServletResponse responseheader, @RequestHeader("Authentication") String auth ) {
        responseheader.setHeader("Accept", "application/jason");
        User user = userService.getUserByAuthentication(auth);

        Card userCard = DTOMapper.INSTANCE.convertCardPostDTOtoEntity(cardPostDTO);
        Deck theDeck = deckService.getDeckById(deckId);
        if(!user.getDeckList().contains(deckService.getDeckById(deckId))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User by Authentication can't alter/acess this resource !");
        }
        Card deckCard = cardService.createCard(userCard, theDeck.getTemplate());
        Deck deckToReturn = deckService.addNewCard(deckCard, deckId);
        //The template doesn't have to be transformed to a TemplateGetDTO since no info must be left out
        return deckToReturn;
    }

    @PutMapping("/decks/{deckId}/cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void changeCard(@PathVariable Long deckId, @PathVariable Long cardId, @RequestBody CardPutDTO cardPutDTO, @RequestHeader("Authentication") String auth ){
        Card putCard = DTOMapper.INSTANCE.convertCardPutDTOtoEntity(cardPutDTO);
        if(!Objects.equals(cardId, cardPutDTO.getCardId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The cardId in the Path doesn't match the Id from the Card Put Entity.");
        }
        User user = userService.getUserByAuthentication(auth);
        if(!user.getDeckList().contains(deckService.getDeckById(deckId))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User by Authentication can't alter/acess this resource !");
        }
        deckService.checkIfCardIdIsInDeck(putCard.getCardId(), deckId);
        cardService.changeCard(putCard, deckService.getDeckById(deckId).getTemplate());


    }

    @GetMapping("cards/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Card getCard(@PathVariable Long cardId, @RequestHeader("Authentication") String auth){
        Card cardToReturn = cardService.getCardById(cardId);
        Deck deck = deckRepository.findByCardListContaining(cardToReturn);
        User user = userService.getUserByAuthentication(auth);
        if(!user.getDeckList().contains(deck)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User from the provided Authentication doesn't have access to this resource");
        }
        return cardToReturn;


    }

    @DeleteMapping("/decks/{deckId}/cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteCard(@PathVariable Long deckId,@PathVariable Long cardId, @RequestHeader("Authentication") String auth){
        User user = userService.getUserByAuthentication(auth);
        if(!user.getDeckList().contains(deckService.getDeckById(deckId))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The User from the provided Authentication doesn't have access to this resource");
        }

        deckService.checkIfCardIdIsInDeck(deckId, cardId);
        cardService.deleteCard(cardId);
    }





}
