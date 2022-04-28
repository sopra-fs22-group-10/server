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
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;


import javax.persistence.EntityManager;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DeckController deckController;


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
    private User user;
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

        user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setAuthentication("testAuthentication");
        user.setStatus(UserStatus.OFFLINE);

        user.setUserId(1L);


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
        MockHttpServletRequestBuilder getRequest = get("/decks").contentType(MediaType.APPLICATION_JSON).header("Authentication", user.getAuthentication());

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
                .header("Authentication", user.getAuthentication())
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

        List<Deck> deckList = new ArrayList<>();
        deckList.add(testDeck);
        user.setDeckList(deckList);

        given(deckService.getDeckById(testDeck.getDeckId())).willReturn(testDeck);
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        

        MockHttpServletRequestBuilder getRequest = get("/decks/1").header("Authentication", user.getAuthentication()).contentType(MediaType.APPLICATION_JSON);

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
        List<Deck> deckList = new ArrayList<>();
        deckList.add(testDeck);
        user.setDeckList(deckList);

        given(templateService.createTemplate(testTemplate)).willReturn(testTemplate);
        given(deckService.setTemplate(Mockito.any(),Mockito.any())).willReturn(testDeck);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(user);
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);

        MockHttpServletRequestBuilder postRequest = post("/decks/1/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", user.getAuthentication())
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

        MockHttpServletRequestBuilder getRequest = get("/decks/2").header("Authentication", user.getAuthentication());

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

        List<Deck> deckList = new ArrayList<>();
        deckList.add(testDeck);
        user.setDeckList(deckList);

        given(cardService.createCard(Mockito.any(), Mockito.any())).willReturn(testCard);
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(user);

        List<Card> cardlist = new ArrayList<>();
        cardlist.add(testCard);
        testDeck.setCardList(cardlist);

        given(deckService.addNewCard(Mockito.any(), Mockito.any())).willReturn(testDeck);

        MockHttpServletRequestBuilder postRequest = post("/decks/1/cards")
                .header("Authentication", user.getAuthentication())
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
        List<Deck> deckList = new ArrayList<>();
        deckList.add(testDeck);
        user.setDeckList(deckList);

        given(deckService.checkIfCardIdIsInDeck(Mockito.any(), Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided CardId doesn't correspond to a Card in the Deck." ));
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder deleteRequest = delete("/decks/1/cards/1")
                .header("Authentication", user.getAuthentication())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest).andExpect(status().isNotFound());

    }

    @Test
    public void change_Card_with_valid_Input() throws Exception{

        CardPutDTO cardPutDTO = new CardPutDTO();

        Stat postStat = new Stat();

        Stat cardStat2 = new Stat();
        cardStat2.setStatvalue("300");
        cardStat2.setStatname("cardStat1");
        cardStat2.setStatId(1L);
        cardStat2.setStattype(StatTypes.NUMBER);


        Template testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(templateStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setTemplateId(1L);


        List<Stat> cardStats = new ArrayList<>();
        cardStats.add(cardStat2);
        cardPutDTO.setCardstats(cardStats);
        cardPutDTO.setCardId(1L);
        cardPutDTO.setCardname("testCard1");
        cardPutDTO.setImage("newImage");

        testDeck.setTemplate(testTemplate);

        List<Deck> deckList = new ArrayList<>();
        deckList.add(testDeck);
        List<Card> cardlist = new ArrayList<>();
        cardlist.add(testCard);
        testDeck.setCardList(cardlist);
        user.setDeckList(deckList);

        given(cardService.createCard(Mockito.any(), Mockito.any())).willReturn(testCard);
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(user);
        doNothing().when(cardService).changeCard(Mockito.any(),Mockito.any());


        MockHttpServletRequestBuilder putRequest = put("/decks/" + testDeck.getDeckId()+"/cards/"+ cardPutDTO.getCardId())
                .header("Authentication", user.getAuthentication())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cardPutDTO));

        mockMvc.perform(putRequest).andExpect(status().isNoContent());
                //.andExpect(jsonPath("$.cardList", hasSize(1)))



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
        TestUser2.setUsername("Username2");
        TestUser2.setStatus(UserStatus.ONLINE);
        TestUser2.setPassword("password");
        TestUser2.setAuthentication("34");

        Deck testDeck2 = new Deck();
        testDeck2.setDeckId(1L);
        testDeck2.setDeckname("Yess");
        testDeck2.setDeckstatus(DeckStatus.PUBLIC);
        testDeck2.setTemplate(testTemplate2);

        DeckPutDTO deckPutDTO = new DeckPutDTO();
        deckPutDTO.setDeckId(1L);


        given(userService.getUserByID(Mockito.any())).willReturn(TestUser2);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(TestUser2);
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
                .header("Authentication", TestUser2.getAuthentication())
                .content(asJsonString(deckPutDTO));

        mockMvc.perform(putRequest).andExpect(status().isNoContent());

    }
    @Test
    public void getUserDecks_correct_Input() throws Exception{

        List<Card> cardlist = new ArrayList<>();
        cardlist.add(testCard);
        testDeck.setCardList(cardlist);
        testDeck.setTemplate(testTemplate);

        List<Deck> decklist = new ArrayList<>();
        decklist.add(testDeck);

        given(userService.getUserByID(Mockito.any())).willReturn(user);
        given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(user);


        user.setDeckList(decklist);

        // then
        MockHttpServletRequestBuilder getRequest = get("/decks/users/"+user.getUserId())
                .header("Authentication", user.getAuthentication())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(getRequest).andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].deckname", is(testDeck.getDeckname())))
                .andExpect(jsonPath("$[0].deckstatus", is(testDeck.getDeckstatus().toString())))
                .andExpect(jsonPath("$.[0].deckname", is(testDeck.getDeckname())))
                .andExpect(jsonPath("$.[0].template.templatestats[0].statvalue",is(testTemplate.getTemplatestats().get(0).getStatvalue())))
                .andExpect(jsonPath("$.[0].template.templatestats[0].statname",is(testTemplate.getTemplatestats().get(0).getStatname())))
                .andExpect(jsonPath("$.[0].cardList[0].cardstats[0].statvalue",is(testCard.getCardstats().get(0).getStatvalue())))
                .andExpect(jsonPath("$.[0].cardList[0].cardstats[0].statname",is(testCard.getCardstats().get(0).getStatname())))
                .andExpect(jsonPath("$.[0].cardList[0].cardstats[0].stattype",is(testCard.getCardstats().get(0).getStattype().toString())))
                .andExpect(jsonPath("$.[0].cardList[0].cardstats[0].valuestypes",is(testCard.getCardstats().get(0).getValuestypes())));



    }


    @Test //post /users -> 201 : successful register
    public void createUser_validInput_userCreated() throws Exception {
        //Mocking AddExisting Deck to User



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
        List<Card> cardlist = new ArrayList<>();
        cardlist.add(testCard2);
            testDeck2.setCardList(cardlist);

            DeckPutDTO deckPutDTO = new DeckPutDTO();
            deckPutDTO.setDeckId(1L);

            given(userService.getUserByID(Mockito.any())).willReturn(TestUser2);
            given(deckService.getDeckById(Mockito.any())).willReturn(testDeck);
            given(cardService.createCard(Mockito.any(), Mockito.any())).willReturn(testCard2);
            given(deckService.createDeck(Mockito.any())).willReturn(testDeck2);
            given(templateService.createTemplate(Mockito.any())).willReturn(testTemplate2);




        // given predefined user
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(user.getPassword());


        doAnswer(invocation -> {
            List<Deck> decklist = new ArrayList<>();
            decklist.add(testDeck2);
            user.setDeckList(decklist);


            return null;
        }).when(userService).addDeck(Mockito.any(), Mockito.any());

        given(userService.createUser(Mockito.any())).willReturn(user);
        given(userService.getUserByID(Mockito.any())).willReturn(user);
        given(userService.getUserByAuthentication(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLoginDTO));

        // then
        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.deckList[0].deckname", is(testDeck2.getDeckname())))
                .andExpect(jsonPath("$.deckList[0].template.templatestats[0].statvalue",is(testTemplate2.getTemplatestats().get(0).getStatvalue())))
                .andExpect(jsonPath("$.deckList[0].template.templatestats[0].statname",is(testTemplate2.getTemplatestats().get(0).getStatname())))
                .andExpect(jsonPath("$.deckList[0].cardList[0].cardstats[0].statvalue",is(testCard2.getCardstats().get(0).getStatvalue())))
                .andExpect(jsonPath("$.deckList[0].cardList[0].cardstats[0].statname",is(testCard2.getCardstats().get(0).getStatname())))
                .andExpect(jsonPath("$.deckList[0].cardList[0].cardstats[0].stattype",is(testCard2.getCardstats().get(0).getStattype().toString())))
                .andExpect(jsonPath("$.deckList[0].cardList[0].cardstats[0].valuestypes",is(testCard2.getCardstats().get(0).getValuestypes())))
                .andExpect(jsonPath("$.deckList[0].cardList[0].cardname", is(testCard2.getCardname())))
                .andReturn();
        String authentication = mvcResult.getResponse().getHeader("Authentication");
        assertEquals(authentication, user.getAuthentication());
    }

    @Test //post /users -> 409 : register for taken username
    public void createUser_usernameTaken() throws Exception {
        // given predefined user
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(user.getUsername());
        userLoginDTO.setPassword(user.getPassword());
        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Username is taken!"));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)

                .content(asJsonString(userLoginDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(409));
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
