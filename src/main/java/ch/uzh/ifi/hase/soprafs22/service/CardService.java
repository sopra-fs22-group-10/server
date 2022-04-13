package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.CardRepository;
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
public class CardService {
    private final Logger log = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;
    private final StatService statService;

    @Autowired
    public CardService(@Qualifier("cardRepository") CardRepository cardRepository, StatService statService) {
        this.cardRepository = cardRepository;
        this.statService = statService;
    }

    public List<Card> getCards() {
        return this.cardRepository.findAll();
    }



    public Card createCard(Card newCard, Template cardTemplate) {

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        if(cardTemplate == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cards can't be created with a empty Template");
        }

        newCard = checkCardMatchesTemplateAndHasValidStats(newCard, cardTemplate);

        newCard = cardRepository.save(newCard);
        cardRepository.flush();

        log.debug("Created Information for Card: {}", newCard);
        return newCard;
    }

    //Checks if the Card and Template have the same Stat Structure and then creates the new Stats, adds the to the Card Entity and returns the updated one.
    public Card checkCardMatchesTemplateAndHasValidStats(Card card, Template template){
        if(card.getCardstats().size() != template.getStatcount()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Card StatCount doesn't match the template StatCount");
        }


        List<Stat> newStats = new ArrayList<>();
        for(int i = 0; i<card.getCardstats().size(); i++){
             Stat StatCard= card.getCardstats().get(i);
             Stat StatTemplate = template.getTemplatestats().get(i);
             if(StatCard.getStattype() != StatTemplate.getStattype() || StatCard.getValuestypes() != StatTemplate.getValuestypes()){
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The StatType or ValuesType of the card and Template doesn't match!");
             }
             //Only Stat with StatType Value can have a ValuesType
             if((StatCard.getStattype()!= StatTypes.VALUE && (StatCard.getValuestypes() != null) || (StatCard.getStattype()== StatTypes.VALUE && (StatCard.getValuestypes() == null)))){
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only StatType VALUE, can and must have a non null Valuestype");
             }
             if(i+1<card.getCardstats().size()){
                 if(Objects.equals(StatCard.getStatname(), card.getCardstats().get(i + 1).getStatname())){
                     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No two stats of the same Card can have the same names");
                 }
             }
             if(StatCard.getStatvalue() == null){
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A CardStat needs to have a Value");
             }
            newStats.add(statService.createStat(StatCard));

        }
        card.setCardstats(newStats);

        return card;
    }

}