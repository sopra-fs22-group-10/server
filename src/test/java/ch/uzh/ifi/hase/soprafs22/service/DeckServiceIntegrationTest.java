package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.CardRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import ch.uzh.ifi.hase.soprafs22.repository.TemplateRepository;
import org.junit.jupiter.api.AfterEach;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DeckPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
public class DeckServiceIntegrationTest {

    @Qualifier("deckRepository")
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private DeckService deckService;

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private CardService cardService;



    @BeforeEach
    public void setup() {
        deckRepository.deleteAll();
    }

    @AfterEach
    public void teardown(){
        deckRepository.deleteAll();
        templateRepository.deleteAll();
        cardRepository.deleteAll();
        statRepository.deleteAll();
    }

    @Test
    public void createDeck_validInputs_success() {
        // given
        assertEquals(Collections.emptyList(), deckRepository.findBydeckstatus(DeckStatus.PUBLIC));

        Deck testDeck = new Deck();

        // when
        Deck createdDeck = deckService.createDeck(testDeck);
        // then
        assertEquals(testDeck.getDeckId(), createdDeck.getDeckId());
        assertEquals("Deck Nr."+testDeck.getDeckId(), createdDeck.getDeckname());
        assertEquals(DeckStatus.PUBLIC, createdDeck.getDeckstatus());
    }

    @Test
    public void setTemplate_with_valid_INPUT(){
        Template testTemplate = new Template();


        Stat testStat = new Stat();
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);

        Deck testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);

        testDeck = deckService.createDeck(testDeck);
        testTemplate = templateService.createTemplate(testTemplate);

        Deck newDeck = deckService.setTemplate(testTemplate, testDeck.getDeckId());


        assertEquals(newDeck.getTemplate().getTemplateId(), testTemplate.getTemplateId());
        assertEquals(newDeck.getTemplate().getTemplatestats().size(), testTemplate.getTemplatestats().size());
        assertEquals(newDeck.getTemplate().getTemplatestats(), testTemplate.getTemplatestats());


    }

    @Test
    public void addCard_with_valid_INPUT(){

        Deck testDeck = new Deck();
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);


        Stat templateStat = new Stat();
        templateStat.setStatname("Stat1");
        templateStat.setStattype(StatTypes.NUMBER);

        Stat cardStat = new Stat();
        cardStat.setStatvalue(200.0);
        cardStat.setStatname("Stat1");
        cardStat.setStattype(StatTypes.NUMBER);

        Template testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);


        Card testCard = new Card();
        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardname("testCard1");
        testCard.setImage("ImageLink");


        // given
        testDeck = deckService.createDeck(testDeck);

        Card createdCard = cardService.createCard(testCard, testTemplate);
        

        Deck newDeck = deckService.addNewCard(createdCard, testDeck.getDeckId());



        assertEquals(newDeck.getCardList().get(0).getCardId(), createdCard.getCardId());
        assertEquals(newDeck.getCardList().get(0).getCardname(), createdCard.getCardname());
        assertEquals(newDeck.getCardList().get(0).getImage(), createdCard.getImage());
        assertEquals(newDeck.getCardList().get(0).getCardstats(), createdCard.getCardstats());
    }

    @Test
    public void addCard_when_Card_in_Deck(){

        Deck testDeck = new Deck();
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);


        Stat templateStat = new Stat();
        templateStat.setStatname("Stat1");
        templateStat.setStattype(StatTypes.NUMBER);

        Stat cardStat = new Stat();
        cardStat.setStatvalue(200.0);
        cardStat.setStatname("Stat1");
        cardStat.setStattype(StatTypes.NUMBER);

        Template testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);


        Card testCard = new Card();
        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardname("testCard1");
        testCard.setImage("ImageLink");

        testDeck = deckService.createDeck(testDeck);
        Card createdCard = cardService.createCard(testCard, testTemplate);
        Deck newDeck = deckService.addNewCard(createdCard, testDeck.getDeckId());


        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> deckService.addNewCard(testCard, newDeck.getDeckId()));
        assertEquals(thrown.getStatus(), HttpStatus.CONFLICT);

    }
    @Test
    public void addCard_when_CardName_exists_in_Deck() {

        Stat templateStat = new Stat();
        templateStat.setStatname("cardStat1");
        templateStat.setStattype(StatTypes.NUMBER);


        Template testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);


        Stat cardStat = new Stat();
        cardStat.setStatvalue(200.0);
        cardStat.setStatname("cardStat1");
        cardStat.setStattype(StatTypes.NUMBER);
        List<Stat> statList = new ArrayList<>();
        statList.add(cardStat);

        Stat cardStat2 = new Stat();
        cardStat2.setStatvalue(300.0);
        cardStat2.setStatname("cardStat1");
        cardStat2.setStattype(StatTypes.NUMBER);
        List<Stat> statList2 = new ArrayList<>();
        statList2.add(cardStat2);

        Card testCard = new Card();
        testCard.setCardstats(statList);
        testCard.setCardname("testCard1");
        testCard.setImage("someImage");
        testCard = cardService.createCard(testCard, testTemplate);

        Card testCard2 = new Card();
        testCard2.setCardstats(statList2);
        testCard2.setCardname("testCard1");
        testCard2.setImage("someImage2");
        Card newCard = cardService.createCard(testCard2, testTemplate);

        // given
        Deck testDeck = new Deck();
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);

        testDeck = deckService.createDeck(testDeck);
        Deck finalDeck = deckService.addNewCard(testCard, testDeck.getDeckId());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> deckService.addNewCard(newCard, finalDeck.getDeckId()));
        assertEquals(thrown.getStatus(), HttpStatus.CONFLICT);
    }


}

