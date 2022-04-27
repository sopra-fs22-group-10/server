package ch.uzh.ifi.hase.soprafs22.controller;

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
    public List<Deck> getAllDecks() {
        // fetch all decks in the internal representation
        List<Deck> decks = deckService.getPublicDecks();

        //Authentification Check

        return decks;
    }

    @GetMapping("/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Deck getDeckById(@PathVariable Long deckId) {
        // fetch all decks in the internal representation
        Deck deck = deckService.getDeckById(deckId);

        //Authentification Check

        return deck;
    }
    //Needs rework, since it doesn't work properly
    @DeleteMapping("/decks/{deckId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteDeck(@PathVariable Long deckId){
        Deck deckToDelete = deckService.getDeckById(deckId);
        for(User user: userService.getUsers()){
            if(user.getDeckList().contains(deckToDelete)){
                userService.removeDeck(deckId, user.getUserId());
            }
        }

        deckRepository.deleteById(deckId);
    }


    @PostMapping("/decks/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck createDeck(@PathVariable Long userId, @RequestBody DeckPostDTO deckPostDTO){

        Deck newDeck = DTOMapper.INSTANCE.convertDeckPostDTOtoEntity(deckPostDTO);

        //In create Deck the userId has to be added to link decks with user accounts
        Deck createdDeck = deckService.createDeck(newDeck);
        userService.addDeck(createdDeck.getDeckId(), userId);

        return createdDeck;
    }

    @PutMapping("/decks/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void AddExistingDeckToUser(@PathVariable Long userId, @RequestBody DeckPutDTO deckPutDTO){
        //Check if user exists
        User user = userService.getUserByID(userId);

        Long deckId = deckPutDTO.getDeckId();
        Deck existingDeck = deckService.getDeckById(deckId);
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
    public Deck CreateTemplate(@PathVariable Long deckId, @RequestBody TemplatePostDTO templatePostDTO, HttpServletResponse responseheader ){
        responseheader.setHeader("Accept", "application/jason");
        Template userTemplate = DTOMapper.INSTANCE.convertTemplatePostDTOtoEntity(templatePostDTO);
        Template deckTemplate = templateService.createTemplate(userTemplate);

        //The template doesn't have to be transformed to a TemplateGetDTO since no info must be left out
        return deckService.setTemplate(deckTemplate, deckId);
    }

    @PostMapping("/decks/{deckId}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck CreateCard(@PathVariable Long deckId, @RequestBody CardPostDTO cardPostDTO, HttpServletResponse responseheader ) {
        responseheader.setHeader("Accept", "application/jason");

        Card userCard = DTOMapper.INSTANCE.convertCardPostDTOtoEntity(cardPostDTO);
        Deck theDeck = deckService.getDeckById(deckId);

        Card deckCard = cardService.createCard(userCard, theDeck.getTemplate());
        Deck deckToReturn = deckService.addNewCard(deckCard, deckId);
        //The template doesn't have to be transformed to a TemplateGetDTO since no info must be left out
        return deckToReturn;
    }

    @PutMapping("/decks/{deckId}/cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void changeCard(@PathVariable Long deckId, @PathVariable Long cardId, @RequestBody CardPutDTO cardPutDTO){
        Card putCard = DTOMapper.INSTANCE.convertCardPutDTOtoEntity(cardPutDTO);
        if(!Objects.equals(cardId, cardPutDTO.getCardId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The cardId in the Path doesn't match the Id from the Card Put Entity.");
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
    public void deleteCard(@PathVariable Long deckId,@PathVariable Long cardId){

        deckService.checkIfCardIdIsInDeck(deckId, cardId);
        cardService.deleteCard(cardId);
    }




}
