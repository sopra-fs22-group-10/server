package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.repository.CardRepository;
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
public class CardService {
    private final Logger log = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;

    @Autowired
    public CardService(@Qualifier("cardRepository") CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCards() {
        return this.cardRepository.findAll();
    }



    public Card createCard(Card newCard) {

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newCard = cardRepository.save(newCard);
        cardRepository.flush();

        log.debug("Created Information for Card: {}", newCard);
        return newCard;
    }



}