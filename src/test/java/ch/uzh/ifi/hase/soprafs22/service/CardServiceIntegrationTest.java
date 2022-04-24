package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.constant.ValuesTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.CardRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class CardServiceIntegrationTest {

    @Qualifier("cardRepository")
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private StatService statService;

    private Card testCard;
    private Template testTemplate;
    private Stat TemplateStat;
    private Stat CardStat;


    @BeforeEach
    public void setup() {
        cardRepository.deleteAll();


        testCard = new Card();
        testCard.setCardname("testCardname1");
        testCard.setImage("randomLink");

        testTemplate = new Template();



        TemplateStat = new Stat();
        TemplateStat.setStatname("testStat1");
        TemplateStat.setStattype(StatTypes.NUMBER);

        CardStat = new Stat();
        CardStat.setStatname("testStat1");
        CardStat.setStattype(StatTypes.NUMBER);
        CardStat.setStatvalue("CardStatValue");

        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(CardStat);
        testCard.setCardstats(cardStats);

        List<Stat> templateStat = new ArrayList<>();
        templateStat.add(TemplateStat);
        testTemplate.setTemplatestats(templateStat);


    }
    @AfterEach
    public void teardown(){
        cardRepository.deleteAll();
        statRepository.deleteAll();
    }

    @Test
    public void create_Card_success(){


        Card createdCard = cardService.createCard(testCard, testTemplate);

        // then
        assertEquals(testCard.getCardId(), createdCard.getCardId());
        assertEquals(testCard.getCardname(), createdCard.getCardname());
        assertEquals(testCard.getCardstats(), createdCard.getCardstats());
    }

    @Test
    public void create_Card_invalidINPUT_null_Template(){

        Template testTemplate2 = new Template();

        //If cardtemplate Null
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> cardService.createCard(testCard, testTemplate2));
        assertEquals(thrown.getStatus(), HttpStatus.CONFLICT);

    }

    @Test
    public void create_Card_invalidINPUT_different_Statcount(){
        Stat TemplateStat2 = new Stat();
        TemplateStat2.setStatname("testTemplateStat1");
        TemplateStat2.setStatId(1L);
        TemplateStat2.setStattype(StatTypes.NUMBER);

        List<Stat> templateStats = testTemplate.getTemplatestats();
        templateStats.add(TemplateStat2);
        testTemplate.setTemplatestats(templateStats);

        assertThrows(ResponseStatusException.class,()-> cardService.createCard(testCard, testTemplate));

    }

    @Test
    public void create_Card_invalidINPUT_not_matching_StatType(){
        Stat TemplateStat2 = new Stat();
        TemplateStat2.setStatname("testTemplateStat1");
        TemplateStat2.setStatId(1L);
        TemplateStat2.setStattype(StatTypes.STARS);

        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(TemplateStat2);
        testTemplate.setTemplatestats(templateStats);

        assertThrows(ResponseStatusException.class,()-> cardService.createCard(testCard, testTemplate));
    }

    @Test
    public void create_Card_invalidINPUT_not_null_ValuesType(){

        Stat CardStat2 = new Stat();
        CardStat2.setStatname("testCardStat1");
        CardStat2.setStatvalue("3");
        CardStat2.setStatId(1L);
        CardStat2.setStattype(StatTypes.STARS);
        CardStat2.setValuestypes(ValuesTypes.KMH);

        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(CardStat2);
        testCard.setCardstats(cardStats);

        assertThrows(ResponseStatusException.class,()-> cardService.createCard(testCard, testTemplate));
        
    }

    @Test
    public void delete_Card_success(){
        Card newcard = cardService.createCard(testCard, testTemplate);
        List<Card> allCards = cardRepository.findAll();
        assertEquals(1, allCards.size());
        cardService.deleteCard(newcard.getCardId());
        //cardRepository.existsById(newcard.getCardId());
        allCards = cardRepository.findAll();
        assertEquals(0, allCards.size());
    }

    @Test
    public void createCard_invalid_INPUT_two_stats_with_same_statname() {
        // when -> any object is being saved in the statRepository -> return the dummy
        // testStat
        Stat TemplateStat2 = new Stat();
        TemplateStat2.setStatname("testTemplateStat1");
        TemplateStat2.setStatId(1L);
        TemplateStat2.setStattype(StatTypes.NUMBER);
        
        List<Stat> cardStats = testCard.getCardstats();
        cardStats.add(CardStat);
        testCard.setCardstats(cardStats);

        List<Stat> templateStats = testTemplate.getTemplatestats();
        templateStats.add(TemplateStat2);
        testTemplate.setTemplatestats(templateStats);
        
        assertThrows(ResponseStatusException.class,()-> cardService.createCard(testCard, testTemplate));

    }

    @Test
    public void change_Card_success(){

        testCard = cardService.createCard(testCard, testTemplate);

        Stat newCardStat = new Stat();
        newCardStat.setStatId(testCard.getCardstats().get(0).getStatId());
        newCardStat.setStatname("testStat1");
        newCardStat.setStattype(StatTypes.NUMBER);
        newCardStat.setStatvalue("33");

        Card newcard = new Card();
        newcard.setCardId(testCard.getCardId());
        newcard.setCardname("NewCardname");
        newcard.setImage("newImage");


        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(newCardStat);
        newcard.setCardstats(cardStats);

        cardService.changeCard(newcard, testTemplate);

        Card changedCard = cardService.getCardById(newcard.getCardId());

        assertEquals(changedCard.getCardId(), newcard.getCardId());
        assertEquals(changedCard.getCardstats().get(0).getStatvalue(), "33");
        assertEquals(changedCard.getCardname(), "NewCardname");
        assertEquals(changedCard.getImage(), newcard.getImage());
        assertEquals(changedCard.getCardstats().get(0).getStatId(), newcard.getCardstats().get(0).getStatId());
        assertEquals(changedCard.getCardstats().get(0).getStattype(), newcard.getCardstats().get(0).getStattype());
        assertEquals(changedCard.getCardstats().get(0).getStatvalue(), newcard.getCardstats().get(0).getStatvalue());




    }




}
