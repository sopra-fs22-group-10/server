package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.CardService;
import ch.uzh.ifi.hase.soprafs22.service.DeckService;
import ch.uzh.ifi.hase.soprafs22.service.TemplateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class DeckController {

    private final DeckService deckService;
    private final TemplateService templateService;
    private final CardService cardService;
    private final UserService userService;
    private final DeckRepository deckRepository;

    DeckController(DeckService deckService, DeckRepository deckRepository, TemplateService templateService, CardService cardService, UserService userService) {
        this.deckService = deckService;
        this.templateService = templateService;
        this.cardService = cardService;
        this.userService = userService;
        this.deckRepository = deckRepository;

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

    @DeleteMapping("/deck/{deckId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteDeck(@PathVariable Long deckId){
        Deck deckToDelete = deckService.getDeckById(deckId);

        deckRepository.deleteById(deckId);
    }


    @PostMapping("/decks/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck createDeck(@PathVariable Long userId, DeckPostDTO deckPostDTO){

        Deck newDeck = DTOMapper.INSTANCE.convertDeckPostDTOtoEntity(deckPostDTO);

        //In create Deck the userId has to be added to link decks with user accounts
        Deck createdDeck = deckService.createDeck(newDeck);
        userService.addDeck(createdDeck.getDeckId(), userId);

        return createdDeck;
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
    public void changeCard(@PathVariable Long deckId, @RequestBody CardPutDTO cardPutDTO){
        Card putCard = DTOMapper.INSTANCE.convertCardPutDTOtoEntity(cardPutDTO);
        deckService.checkIfCardIdIsInDeck(deckId, putCard.getCardId());
        cardService.changeCard(putCard, deckService.getDeckById(deckId).getTemplate());


    }

    @DeleteMapping("/decks/{deckId}/cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteCard(@PathVariable Long deckId, Long cardId){

        deckService.checkIfCardIdIsInDeck(deckId, cardId);
        cardService.deleteCard(cardId);
    }




}
