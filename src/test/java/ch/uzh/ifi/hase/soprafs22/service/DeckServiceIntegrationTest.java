package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import org.junit.jupiter.api.AfterEach;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DeckPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;


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



    @BeforeEach
    public void setup() {
        deckRepository.deleteAll();
    }

    @AfterEach
    public void teardown(){deckRepository.deleteAll();}

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
        testTemplate.setTemplateId(1L);
        testTemplate.setStatcount(0);
        testTemplate.setTemplatename("testTemplatename");

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

        Deck newDeck = deckService.setTemplate(testTemplate, testDeck.getDeckId());


        assertEquals(newDeck.getTemplate().getTemplateId(), testTemplate.getTemplateId());
        assertEquals(newDeck.getTemplate().getTemplatename(), testTemplate.getTemplatename());
        assertEquals(newDeck.getTemplate().getStatcount(), testTemplate.getStatcount());
        assertEquals(newDeck.getTemplate().getTemplatestats(), testTemplate.getTemplatestats());
    }

    @Test
    public void addCard_with_valid_INPUT(){
        Stat templateStat = new Stat();
        templateStat.setStatname("templateStat1");
        templateStat.setStatId(1L);
        templateStat.setStattype(StatTypes.NUMBER);

        Stat cardStat = new Stat();
        cardStat.setStatvalue("200");
        cardStat.setStatname("cardStat1");
        cardStat.setStatId(1L);
        cardStat.setStattype(StatTypes.NUMBER);

        Template testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<Stat>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setTemplateId(1L);
        testTemplate.setStatcount(1);
        testTemplate.setTemplatename("testTemplate1");

        Card testCard = new Card();
        List<Stat> cardStats = new ArrayList<Stat>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardId(1L);
        testCard.setCardname("testCard1");


        // given
        Deck testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);
        testDeck.setTemplate(testTemplate);

        Deck newDeck = deckService.addNewCard(testCard, testDeck.getDeckId());


        assertEquals(newDeck.getCardList().get(0).getCardId(), testCard.getCardId());
        assertEquals(newDeck.getCardList().get(0).getCardname(), testCard.getCardname());
        assertEquals(newDeck.getCardList().get(0).getImage(), testCard.getImage());
        assertEquals(newDeck.getCardList().get(0).getCardstats(), testCard.getCardstats());
    }

    @Test
    public void addCard_when_Card_in_Deck(){

        Stat cardStat = new Stat();
        cardStat.setStatvalue("200");
        cardStat.setStatname("cardStat1");
        cardStat.setStatId(1L);
        cardStat.setStattype(StatTypes.NUMBER);

        Card testCard = new Card();
        List<Stat> cardStats = new ArrayList<Stat>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardId(1L);
        testCard.setCardname("testCard1");

        // given
        Deck testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);
        deckService.addNewCard(testCard, testDeck.getDeckId());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> deckService.addNewCard(testCard, testDeck.getDeckId()));
        assertEquals(thrown.getStatus(), HttpStatus.CONFLICT);

    }
    @Test
    public void addCard_when_CardName_exists_in_Deck() {

        Stat cardStat = new Stat();
        cardStat.setStatvalue("200");
        cardStat.setStatname("cardStat1");
        cardStat.setStatId(1L);
        cardStat.setStattype(StatTypes.NUMBER);

        Card testCard = new Card();
        List<Stat> cardStats = new ArrayList<Stat>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardId(1L);
        testCard.setCardname("testCard1");

        Card testCard2 = new Card();
        List<Stat> cardStats2 = new ArrayList<Stat>();
        cardStats2.add(cardStat);
        testCard2.setCardstats(cardStats);
        testCard2.setCardId(1L);
        testCard2.setCardname("testCard1");


        // given
        Deck testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);

        deckService.addNewCard(testCard, testDeck.getDeckId());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> deckService.addNewCard(testCard2, testDeck.getDeckId()));
        assertEquals(thrown.getStatus(), HttpStatus.BAD_REQUEST);
    }



}

