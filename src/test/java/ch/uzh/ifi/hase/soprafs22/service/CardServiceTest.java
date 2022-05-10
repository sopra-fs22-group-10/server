package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.CardRepository;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private CardService cardService;

    @Mock
    private StatService statService;

    private Card testCard;
    private Stat TemplateStat;
    private Stat CardStat;
    private Template testTemplate;
    private List <Stat> cardStats;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testCard = new Card();
        testCard.setCardId(1L);
        testCard.setCardname("testCardname");
        testCard.setImage("RandomImage");

        testTemplate = new Template();
        testTemplate.setTemplateId(2L);


        TemplateStat = new Stat();
        TemplateStat.setStatname("testStat1");
        TemplateStat.setStatId(3L);
        TemplateStat.setStattype(StatTypes.NUMBER);

        CardStat = new Stat();
        CardStat.setStatname("testStat1");
        CardStat.setStatId(4L);
        CardStat.setStattype(StatTypes.NUMBER);
        CardStat.setStatvalue(6.0);

        cardStats = new ArrayList<>();
        cardStats.add(CardStat);
        testCard.setCardstats(cardStats);

        List<Stat> templateStat = new ArrayList<>();
        templateStat.add(TemplateStat);
        testTemplate.setTemplatestats(templateStat);


        // when -> any object is being saved in the cardRepository -> return the dummy
        // testCard
        Mockito.when(cardRepository.save(Mockito.any())).thenReturn(testCard);
        Mockito.when(statRepository.save(Mockito.any())).thenReturn(CardStat);
        Mockito.when(statService.createStat(Mockito.any())).thenReturn(CardStat);
        //Mockito.when(cardService.checkCardMatchesTemplateAndHasValidStats(Mockito.any(),Mockito.any())).thenReturn(testCard);
    }


    @Test
    public void createCard_success() {
        // when -> any object is being saved in the cardRepository -> return the dummy
        // testCard


        Card createdCard = cardService.createCard(testCard, testTemplate);

        // then

        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testCard.getCardId(), createdCard.getCardId());
        assertEquals(testCard.getCardname(), createdCard.getCardname());
        assertEquals(testCard.getCardstats(), createdCard.getCardstats());

    }
    //Removed duplicate name/password check since passwords should not throw an exception if they are not unique

    
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
        CardStat2.setStatvalue(3.0);
        CardStat2.setStatId(1L);
        CardStat2.setStattype(StatTypes.STARS);
        CardStat2.setValuestypes("KMH");

        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(CardStat2);
        testCard.setCardstats(cardStats);

        assertThrows(ResponseStatusException.class,()-> cardService.createCard(testCard, testTemplate));

    }

    @Test
    public void delete_Card_failure_not_existing_ID(){
        
        Card newcard = cardService.createCard(testCard, testTemplate);

        Mockito.when(cardRepository.existsById(Mockito.any())).thenReturn(false);
        
        assertThrows(ResponseStatusException.class,()-> cardService.deleteCard(newcard.getCardId()));
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

    //This method cant be tested in the cardService test since it has no return value and only interacts with the repository
    /*
    @Test
    public void change_Card_success(){
        Optional <Card> optionalCard = Optional.of(testCard);
        Mockito.when(cardRepository.findById(Mockito.any())).thenReturn(optionalCard);
        Mockito.when(statService.changeStatIfExists(Mockito.any())).thenReturn(false);
        Mockito.when(cardRepository.existsById(Mockito.any())).thenReturn(true);
        Mockito.when(cardRepository.save(Mockito.any())).thenReturn(testCard);


        Card newCard = new Card();
        newCard.setCardname("newCardname");
        newCard.setCardId(1L);
        newCard.setImage("RandomImage");
        
        newCard.setCardstats(cardStats);
        
        cardService.changeCard(newCard,testTemplate);

        assertEquals(testCard.getCardId(), newCard.getCardId());
        assertEquals(testCard.getCardname(), newCard.getCardname());
        assertEquals(testCard.getCardstats(), newCard.getCardstats());
        
    }

     */



}
