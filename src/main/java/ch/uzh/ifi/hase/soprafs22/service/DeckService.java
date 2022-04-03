package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        if(newDeck.getDeckname() == null){
            newDeck.setDeckname("Deck Nr." + newDeck.getDeckId());
        }
        if(newDeck.getDeckStatus() == null){
            newDeck.setDeckStatus(DeckStatus.PUBLIC);
        }
        newDeck = deckRepository.save(newDeck);
        deckRepository.flush();

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



    public Deck getDeckById(Long id){

        //checkIfIDExists(id);
        Optional<Deck> potentialdeck = deckRepository.findById(id);

        if(potentialdeck.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided Deck ID does not exist in the Database.");
        }
        return potentialdeck.get();

    }
}



