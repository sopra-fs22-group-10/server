package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.CardRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
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

    private Card testCard;
    private Template testTemplate;
    private Stat TemplateStat;
    private Stat CardStat;


    @BeforeEach
    public void setup() {
        cardRepository.deleteAll();


        testCard = new Card();
        testCard.setCardId(1L);
        testCard.setCardname("testCardname1");

        testTemplate = new Template();
        testTemplate.setTemplateId(1L);
        testTemplate.setStatcount(1);
        testTemplate.setTemplatename("testTemplatename1");

        TemplateStat = new Stat();
        TemplateStat.setStatname("testTemplateStat1");
        TemplateStat.setStatId(1L);
        TemplateStat.setStattype(StatTypes.NUMBER);

        CardStat = new Stat();
        CardStat.setStatname("testCardStat1");
        CardStat.setStatId(1L);
        CardStat.setStattype(StatTypes.NUMBER);
        CardStat.setStatvalue("CardStatValue");

        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(CardStat);
        testCard.setCardstats(cardStats);

        List<Stat> templateStat = new ArrayList<>();
        templateStat.add(TemplateStat);
        testTemplate.setTemplatestats(templateStat);


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
        assertEquals(thrown.getStatus(), HttpStatus.BAD_REQUEST);

    }

    @Test
    public void create_Card_invalidINPUT_different_Statcount(){

    }

    @Test
    public void create_Card_invalidINPUT_not_matching_StatType(){

    }

    @Test
    public void create_Card_invalidINPUT_not_null_ValuesType(){

    }




}
