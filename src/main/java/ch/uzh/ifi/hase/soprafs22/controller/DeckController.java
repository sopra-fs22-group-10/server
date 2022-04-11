package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DeckGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DeckPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.TemplatePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.DeckService;
import ch.uzh.ifi.hase.soprafs22.service.TemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class DeckController {

    private final DeckService deckService;
    private final TemplateService templateService;

    DeckController(DeckService deckService, TemplateService templateService) {
        this.deckService = deckService;

        this.templateService = templateService;
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


    @PostMapping("/decks/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck createDeck(@PathVariable Long userId, DeckPostDTO deckPostDTO){

        Deck newDeck = DTOMapper.INSTANCE.convertDeckPostDTOtoEntity(deckPostDTO);

        //In create Deck the userId has to be added to link decks with user accounts
        Deck createdDeck = deckService.createDeck(newDeck);

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




}
