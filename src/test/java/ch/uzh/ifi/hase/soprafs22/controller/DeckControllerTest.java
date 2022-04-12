package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DeckPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.TemplatePostDTO;
import ch.uzh.ifi.hase.soprafs22.service.DeckService;
import ch.uzh.ifi.hase.soprafs22.service.TemplateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;
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



import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeckService deckService;

    @MockBean
    private TemplateService templateService;

    private Deck testDeck;
    private Template testTemplate;
    private Stat testStat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testStat = new Stat();
        testStat.setStatvalue("200");
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);

        testTemplate = new Template();
        List<Stat> templateStats = new ArrayList<Stat>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setTemplateId(1L);
        testTemplate.setStatcount(1);
        testTemplate.setTemplatename("testTemplate1");
        // given
        testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckname("Yess");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);
      

        // when -> any object is being save in the deckRepository -> return the dummy
        // testDeck

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

        List<Stat> templateStats = new ArrayList<Stat>();
        TemplatePostDTO template_1 = new TemplatePostDTO();
        templateStats.add(testStat);
        template_1.setTemplatestats(templateStats);
        template_1.setTemplateid(1L);
        template_1.setStatcount(1);
        template_1.setTemplatename("template_1");

        testDeck.setTemplate(testTemplate);

        given(templateService.createTemplate(testTemplate)).willReturn(testTemplate);
        given(deckService.setTemplate(Mockito.any(),Mockito.any())).willReturn(testDeck);

        MockHttpServletRequestBuilder postRequest = post("/decks/1/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(template_1));




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
        map.put("statcount", testTemplate.getStatcount());
        map.put("templatestats", ja);




        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.deckname", is(testDeck.getDeckname())))
                .andExpect(jsonPath("$.deckstatus", is(testDeck.getDeckstatus().toString())))
                .andExpect(jsonPath("$.template.templatename",is(testTemplate.getTemplatename())))
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
    /*
    public void addTemplate(
            
            
    ){}
    
    public void addwrongTemplate(){
        
    }
    */


    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}