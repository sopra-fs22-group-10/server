package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;


import javax.persistence.EntityManager;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private DeckService deckService;

    @MockBean
    private TemplateService templateService;
    
    @MockBean
    private CardService cardService;

    @MockBean
    private StatService statService;

    @MockBean
    private DeckRepository deckRepository;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private StatRepository statRepository;
    @MockBean
    private TemplateRepository templateRepository;


    @MockBean
    EntityManager createentityManager;



    @MockBean
    private UserService userService;

    private Deck testDeck;
    private Template testTemplate;
    private Stat templateStat;
    private Stat cardStat;
    private Card testCard;
    private EntityManager entityManager;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        templateStat = new Stat();
        templateStat.setStatname("templateStat1");
        templateStat.setStatId(1L);
        templateStat.setStattype(StatTypes.NUMBER);

        cardStat = new Stat();
        cardStat.setStatvalue("200");
        cardStat.setStatname("cardStat1");
        cardStat.setStatId(1L);
        cardStat.setStattype(StatTypes.NUMBER);
        

        testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setTemplateId(1L);

        testCard = new Card();
        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(cardStat);
        testCard.setCardstats(cardStats);
        testCard.setCardId(1L);
        testCard.setCardname("testCard1");
        testCard.setImage("hhtpsblablabla");

        // given
        testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);


    }

    @Test
    public void givenDecks_whenGetPublicDecks_thenReturnJsonArray() throws Exception {
        // given
        Deck deck = new Deck();


        deck.setDeckId(1L);
        deck.setDeckstatus(DeckStatus.PUBLIC);
        deck.setDeckname("testDeckname2");
        deck.setTemplate(testTemplate);


        List<Deck> allDecks = Collections.singletonList(deck);

        // this mocks the DeckService -> we define above what the deckService should
        // return when getDecks() is called
        given(deckService.getPublicDecks()).willReturn(allDecks);

        // when
        MockHttpServletRequestBuilder getRequest = get("/decks").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].deckname", is(deck.getDeckname())))
                .andExpect(jsonPath("$[0].deckstatus", is(deck.getDeckstatus().toString())));

    }
    @Test
    public void createDeck_validInput_deckCreated() throws Exception {
        // given


        DeckPostDTO deckPostDTO = new DeckPostDTO();
        deckPostDTO.setDeckname("testDeckname");


        given(deckService.createDeck(Mockito.any())).willReturn(testDeck);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/decks/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(deckPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deckId", is(testDeck.getDeckId().intValue())))
                .andExpect(jsonPath("$.deckname", is(testDeck.getDeckname())))
                .andExpect(jsonPath("$.deckstatus", is(testDeck.getDeckstatus().toString())));
    }

    @Test
    public void get_Deck_with_DeckGet_with_Id() throws Exception{

        testDeck.setTemplate(testTemplate);
        given(deckService.getDeckById(testDeck.getDeckId())).willReturn(testDeck);

        // when/then -> do the request + validate the result
        

        MockHttpServletRequestBuilder getRequest = get("/decks/1").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())

                .andExpect(jsonPath("$.deckname", is(testDeck.getDeckname())))
                .andExpect(jsonPath("$.deckstatus", is(testDeck.getDeckstatus().toString())));
                //.andExpect(jsonPath("$.template", equalToObject(testDeck.getTemplate())));

    }

    @Test
    public void add_Template_to_Deck_with_DeckId() throws Exception{

        List<Stat> templateStats = new ArrayList<>();
        TemplatePostDTO template_1 = new TemplatePostDTO();
        templateStats.add(templateStat);
        template_1.setTemplatestats(templateStats);


        testDeck.setTemplate(testTemplate);

        given(templateService.createTemplate(testTemplate)).willReturn(testTemplate);
        given(deckService.setTemplate(Mockito.any(),Mockito.any())).willReturn(testDeck);

        MockHttpServletRequestBuilder postRequest = post("/decks/1/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(template_1));


        /*
        JSONArray ja = new JSONArray();

        LinkedHashMap<Object,Object> statmap = new LinkedHashMap<>();
        statmap.put("id",testStat.getStatId());
        statmap.put("statvalue", testStat.getStatvalue());
        statmap.put("statname", testStat.getStatname());
        statmap.put("stattype", testStat.getStattype().toString());
        statmap.put("valuestype", testStat.getValuestypes());

        ja.add(statmap);


        LinkedHashMap<Object,Object> map = new LinkedHashMap<>();
        map.put("templateid",testTemplate.getTemplateId().intValue());
        map.put("templatename", testTemplate.getTemplatename());

        map.put("templatestats", ja);
        
         */




        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.deckname", is(testDeck.getDeckname())))
                .andExpect(jsonPath("$.deckstatus", is(testDeck.getDeckstatus().toString())))
                .andExpect(jsonPath("$.template.templateId",is(testTemplate.getTemplateId().intValue())))
                .andExpect(jsonPath("$.template.templatestats[0].statvalue",is(testTemplate.getTemplatestats().get(0).getStatvalue())))
                .andExpect(jsonPath("$.template.templatestats[0].statname",is(testTemplate.getTemplatestats().get(0).getStatname())));

        //Why is template it Linked Hash map ?
    }


    @Test //Get request to Deck but with wrong ID
    public void get_Deck_with_DeckGet_with_wrong_Id() throws Exception{

        // this mocks the DeckService -> we define above what the deckService should
        // return when getDecks() is called
        String baseErrorMessage = "The %s provided does not exist. There %s not any deck assotiated with this Id";

        given(deckService.getDeckById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, "ID", "is")));

        MockHttpServletRequestBuilder getRequest = get("/decks/2");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());


    }

    @Test
    public void createCard_with_valid_INPUT()throws Exception{

        CardPostDTO card_1 = new CardPostDTO();
        Stat postStat = new Stat();
        
        postStat.setStatvalue("200");
        postStat.setStatname("postStat1");
        postStat.setStattype(StatTypes.NUMBER);

        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(postStat);
        
        card_1.setCardstats(cardStats);
        card_1.setCardname("card_1");
        card_1.setImage("https:somestuff.com");

        testDeck.setTemplate(testTemplate);
        
        given(cardService.createCard(Mockito.any(), Mockito.any())).willReturn(testCard);
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);

        List<Card> cardlist = new ArrayList<>();
        cardlist.add(testCard);
        testDeck.setCardList(cardlist);

        given(deckService.addNewCard(Mockito.any(), Mockito.any())).willReturn(testDeck);

        MockHttpServletRequestBuilder postRequest = post("/decks/1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(card_1));
        
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                //.andExpect(jsonPath("$.cardList", hasSize(1)))
                .andExpect(jsonPath("$.cardList[0].cardname",is(testCard.getCardname())))
                .andExpect(jsonPath("$.cardList[0].cardId",is(testCard.getCardId().intValue())))
                .andExpect(jsonPath("$.cardList[0].cardstats[0].statvalue",is(testCard.getCardstats().get(0).getStatvalue())))
                .andExpect(jsonPath("$.cardList[0].cardstats[0].statname",is(testCard.getCardstats().get(0).getStatname())))
                .andExpect(jsonPath("$.cardList[0].cardstats[0].stattype",is(testCard.getCardstats().get(0).getStattype().toString())))
                .andExpect(jsonPath("$.cardList[0].cardstats[0].valuestypes",is(testCard.getCardstats().get(0).getValuestypes())));

        //.andExpect(jsonPath("$.template", equalToObject(testDeck.getTemplate())));
        
    }

    //The testDeck doesn't contain the testCraad to be Deleteed
    @Test
    public void deleteCard_when_Card_not_in_Deck()throws Exception {

        given(deckService.checkIfCardIdIsInDeck(Mockito.any(), Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided CardId doesn't correspond to a Card in the Deck." ));

        MockHttpServletRequestBuilder deleteRequest = delete("/decks/1/cards/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest).andExpect(status().isNotFound());

    }


    @Test
    public void add_Existing_Deck_to_User()throws Exception{

        testDeck.setTemplate(testTemplate);
        List<Card> cardList = new ArrayList<>();
        cardList.add(testCard);
        testDeck.setCardList(cardList);

        User TestUser = new User();
        TestUser.setUsername("Username1");
        TestUser.setStatus(UserStatus.ONLINE);
        TestUser.setPassword("password");
        TestUser.setAuthentication("3");

         Stat templateStat2 = new Stat();
        templateStat2.setStatname("templateStat1");
        templateStat2.setStatId(1L);
        templateStat2.setStattype(StatTypes.NUMBER);

        Stat cardStat2 = new Stat();
        cardStat2.setStatvalue("200");
        cardStat2.setStatname("cardStat1");
        cardStat2.setStatId(1L);
        cardStat2.setStattype(StatTypes.NUMBER);


        Template testTemplate2 = new Template();
        List<Stat> templateStats2 = new ArrayList<>();
        templateStats2.add(templateStat2);
        testTemplate2.setTemplatestats(templateStats2);
        testTemplate2.setTemplateId(1L);

        Card testCard2 = new Card();
        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(cardStat);
        testCard2.setCardstats(cardStats);
        testCard2.setCardId(1L);
        testCard2.setCardname("testCard1");
        testCard2.setImage("hhtpsblablabla");

        //TestUser.setDeckList(new ArrayList<>(List.of(testDeck)));

        User TestUser2 = new User();
        TestUser2.setUserId(33L);
        TestUser.setUsername("Username2");
        TestUser.setStatus(UserStatus.ONLINE);
        TestUser.setPassword("password");
        TestUser.setAuthentication("34");

        Deck testDeck2 = new Deck();
        testDeck2.setDeckId(1L);
        testDeck2.setDeckname("Yess");
        testDeck2.setDeckstatus(DeckStatus.PUBLIC);
        testDeck2.setTemplate(testTemplate2);

        DeckPutDTO deckPutDTO = new DeckPutDTO();
        deckPutDTO.setDeckId(1L);


        given(userService.getUserByID(Mockito.any())).willReturn(TestUser2);
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);
        given(cardService.createCard(Mockito.any(), Mockito.any())).willReturn(testCard2);
        given(deckService.createDeck(Mockito.any())).willReturn(testDeck2);
        given(templateService.createTemplate(Mockito.any())).willReturn(testTemplate2);
        /*doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);

            assertEquals(3, arg0);
            assertEquals("answer me", arg1);
            return null;
        }).when(entityManager).detach(any(Object.class));

         */


        //given(entityManager)).willReturn(entitymanager);
        //doNothing().when(entityManager).detach(Mockito.any());



        MockHttpServletRequestBuilder putRequest = put("/decks/users/33")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(deckPutDTO));

        mockMvc.perform(putRequest).andExpect(status().isNoContent());

    }




    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
