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


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testCard = new Card();
        testCard.setCardId(1L);
        testCard.setCardname("testCardname");

        testTemplate = new Template();
        testTemplate.setTemplateId(1L);
        testTemplate.setStatcount(1);


        TemplateStat = new Stat();
        TemplateStat.setStatname("testStat1");
        TemplateStat.setStatId(1L);
        TemplateStat.setStattype(StatTypes.NUMBER);

        CardStat = new Stat();
        CardStat.setStatname("testStat1");
        CardStat.setStatId(1L);
        CardStat.setStattype(StatTypes.NUMBER);
        CardStat.setStatvalue("CardStatValue");

        List<Stat> cardStats = new ArrayList<>();
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
        StatService statService;



        Card createdCard = cardService.createCard(testCard, testTemplate);

        // then

        Mockito.verify(cardRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testCard.getCardId(), createdCard.getCardId());
        assertEquals(testCard.getCardname(), createdCard.getCardname());
        assertEquals(testCard.getCardstats(), createdCard.getCardstats());

    }
    //Removed duplicate name/password check since passwords should not throw an exception if they are not unique



}
