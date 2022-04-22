package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private DeckService deckService;

    private Deck testDeck;

    @Mock
    private TemplateService templateService;

    @Mock
    private CardService cardService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckstatus(DeckStatus.PUBLIC);
        testDeck.setDeckname("testDeckname");


        Optional <Deck> optionalDeck = Optional.of(testDeck);
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(optionalDeck);
        // when -> any object is being saved in the deckRepository -> return the dummy
        // testDeck
        Mockito.when(deckRepository.save(Mockito.any())).thenReturn(testDeck);
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testDeck));
    }

    @Test
    public void createDeck_validInputs_success() {
        // when -> any object is being saved in the deckRepository -> return the dummy
        // testDeck
        Deck aDeck = new Deck();
        aDeck.setDeckId(1L);
        aDeck.setDeckstatus(DeckStatus.PUBLIC);

        Mockito.when(deckRepository.save(Mockito.any())).thenReturn(aDeck);

        Deck createdDeck = deckService.createDeck(aDeck);

        // then
        Mockito.verify(deckRepository, Mockito.times(2)).save(Mockito.any());

        assertEquals(aDeck.getDeckId(), createdDeck.getDeckId());
        assertEquals(aDeck.getDeckname(), createdDeck.getDeckname());
        assertEquals(DeckStatus.PUBLIC, createdDeck.getDeckstatus());
    }

    @Test
    public void get_Deck_by_Id(){


        Deck createdDeck = deckService.getDeckById(testDeck.getDeckId());

        assertEquals(testDeck.getDeckId(), createdDeck.getDeckId());
        assertEquals(testDeck.getDeckname(), createdDeck.getDeckname());
        assertEquals(DeckStatus.PUBLIC, createdDeck.getDeckstatus());
    }


    @Test
    public void setTemplate_with_valid_INPUT(){


        Template testTemplate = new Template();
        testTemplate.setStatcount(0);


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

        Mockito.when(templateService.createTemplate(Mockito.any())).thenReturn(testTemplate);

        testDeck = deckService.createDeck(testDeck);

        Mockito.verify(deckRepository, Mockito.times(1)).save(Mockito.any());

        testTemplate = templateService.createTemplate(testTemplate);

        Deck newDeck = deckService.setTemplate(testTemplate, testDeck.getDeckId());


        assertEquals(newDeck.getTemplate().getTemplateId(), testTemplate.getTemplateId());
        assertEquals(newDeck.getTemplate().getStatcount(), testTemplate.getStatcount());
        assertEquals(newDeck.getTemplate().getTemplatestats(), testTemplate.getTemplatestats());


    }

    @Test
    public void addCard_with_valid_INPUT(){

        Deck testDeck = new Deck();
        testDeck.setDeckname("Yess");
        testDeck.setDeckId(1L);
        testDeck.setDeckstatus(DeckStatus.PUBLIC);


        Stat templateStat = new Stat();
        templateStat.setStatname("Stat1");
        templateStat.setStatId(2L);
        templateStat.setStattype(StatTypes.NUMBER);

        Stat cardStat = new Stat();
        cardStat.setStatvalue("200");
        cardStat.setStatname("Stat1");
        cardStat.setStatId(3L);
        cardStat.setStattype(StatTypes.NUMBER);

        Template testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setStatcount(1);


        Card testCard = new Card();
        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardId(4L);
        testCard.setCardname("testCard1");
        testCard.setImage("ImageLink");


        // given
        Mockito.when(cardService.createCard(Mockito.any(), Mockito.any())).thenReturn(testCard);

        Optional <Deck> optionalDeck = Optional.of(testDeck);
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(optionalDeck);

        Card createdCard = cardService.createCard(testCard, testTemplate);


        Deck newDeck = deckService.addNewCard(createdCard, testDeck.getDeckId());



        assertEquals(newDeck.getCardList().get(0).getCardId(), createdCard.getCardId());
        assertEquals(newDeck.getCardList().get(0).getCardname(), createdCard.getCardname());
        assertEquals(newDeck.getCardList().get(0).getImage(), createdCard.getImage());
        assertEquals(newDeck.getCardList().get(0).getCardstats(), createdCard.getCardstats());
    }

    @Test
    public void addCard_when_Card_in_Deck(){

        Stat templateStat = new Stat();
        templateStat.setStatname("Stat1");
        templateStat.setStattype(StatTypes.NUMBER);

        Stat cardStat = new Stat();
        cardStat.setStatvalue("200");
        cardStat.setStatname("Stat1");
        cardStat.setStattype(StatTypes.NUMBER);

        Template testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setStatcount(1);


        Card testCard = new Card();
        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardname("testCard1");
        testCard.setImage("ImageLink");

        Mockito.when(cardService.createCard(Mockito.any(), Mockito.any())).thenReturn(testCard);

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
        testTemplate.setStatcount(1);


        Stat cardStat = new Stat();
        cardStat.setStatvalue("200");
        cardStat.setStatname("cardStat1");
        cardStat.setStattype(StatTypes.NUMBER);
        List<Stat> statList = new ArrayList<>();
        statList.add(cardStat);

        Stat cardStat2 = new Stat();
        cardStat2.setStatvalue("300");
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

        Deck finalDeck = deckService.addNewCard(testCard, testDeck.getDeckId());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> deckService.addNewCard(newCard, finalDeck.getDeckId()));
        assertEquals(thrown.getStatus(), HttpStatus.CONFLICT);
    }




    //Removed duplicate name/password check since passwords should not throw an exception if they are not unique

}

