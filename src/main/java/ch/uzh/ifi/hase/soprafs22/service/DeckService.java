package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DeckPutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

@Service
@Transactional
public class DeckService {
    private final Logger log = LoggerFactory.getLogger(DeckService.class);

    private final DeckRepository deckRepository;



    @Autowired
    public DeckService(@Qualifier("deckRepository") DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> getPublicDecks() {
        DeckStatus status = DeckStatus.PUBLIC;

        return this.deckRepository.findBydeckstatus(status);
    }



    public Deck createDeck(Deck newDeck) {
        // saves the given entity but data is only persisted in the database once
        // flush() is called


        if(newDeck.getDeckstatus() == null){
            newDeck.setDeckstatus(DeckStatus.PUBLIC);
        }
        if(newDeck.getDeckname() == null){
            newDeck.setDeckname("Deck.Nr.");
        }


        newDeck = deckRepository.save(newDeck);


        if(Objects.equals(newDeck.getDeckname(), "Deck.Nr.")){
            newDeck.setDeckname("Deck Nr." + newDeck.getDeckId().toString());

            newDeck = deckRepository.save(newDeck);
            deckRepository.flush();
        }


        log.debug("Created Information for Deck: {}", newDeck);
        return newDeck;
    }

    public Deck setTemplate(Template template, Long deckID){
        Deck deck = getDeckById(deckID);
        deck.setTemplate(template);

        deckRepository.flush();

        log.debug("Added template to deck: {}", deck);
        return deck;
    }

    public Deck addNewCard(Card card, Long DeckId){
        Deck deck = getDeckById(DeckId);

        checkIfCardIsAlreadyInDeck(card, deck);
        deck.addCard(card);
        return deck;
    }


    public Deck getDeckById(Long id){
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided Deck ID does not exist in the Database.");
        }
        Optional<Deck> foundDeck = deckRepository.findById(id);

        if(foundDeck.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided Deck ID does not exist in the Database.");
        }

        return foundDeck.get();

    }


    //Only performs check and throws error otherwise, but returns nothing
    public void checkIfCardIsAlreadyInDeck(Card card, Deck deck){

        if(deck.getCardList() != null) {

            if (deck.getCardList().contains(card)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The to be added card exists already in the Deck.");
            }
            for (Card card2 : deck.getCardList()) {
                if (Objects.equals(card.getCardname(), card2.getCardname())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "There already exist a Card with this CardName. CardNames in a Deck have to be unique.");
                }
            }
        }

    }

    public void changeDeck(DeckPutDTO deckPutDTO, Deck originalDeck){
        if(deckPutDTO.getDeckImage() != null && !Objects.equals(deckPutDTO.getDeckImage(), originalDeck.getDeckImage())) {
            originalDeck.setDeckImage(deckPutDTO.getDeckImage());
        }
        if(deckPutDTO.getDeckname() != null && !Objects.equals(deckPutDTO.getDeckname(), originalDeck.getDeckname())){
            originalDeck.setDeckname(deckPutDTO.getDeckname());
        }
        deckRepository.flush();

    }
    //Returns boolean
    public boolean checkIfCardIdIsInDeck(Long cardId, Long deckId){

        for(Card card: getDeckById(deckId).getCardList()){
            if(Objects.equals(card.getCardId(), cardId)){
                return  true;
            }

        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "The provided CardId doesn't correspond to a Card in the Deck.");

    }

    /*
    public void removeCard(Long deckId, Long cardId) {
        Deck deck = getDeckById(deckId);
        List<Card> cardList = deck.getCardList();
        cardList.removeIf(card -> Objects.equals(card.getCardId(), cardId));
        if(cardList.size() == deck.getCardList().size()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The provided");
        }

    }

     */
}



