package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs22.repository.TemplateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class TemplateServiceIntegrationTest {

    @Qualifier("templateRepository")
    @Autowired
    private TemplateRepository templateRepository;

    @Qualifier("deckRepository")
    @Autowired
    private DeckRepository deckRepository;



    @Autowired
    private TemplateService templateService;

    @Autowired
    private StatService statService;



    @BeforeEach
    public void setup() {
        deckRepository.deleteAll();
        templateRepository.deleteAll();
    }

    @AfterEach
    public void teardown(){
        deckRepository.deleteAll();
        templateRepository.deleteAll();
    }

    @Test
    public void createTemplate_validInputs_success() {
        // given
        assertNull(templateRepository.findByTemplateId(1L));


        Template testTemplate = new Template();
        Stat testStat = new Stat();
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);





        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);


        // when
        Template createdTemplate = templateService.createTemplate(testTemplate);

        // then
        assertEquals(testTemplate.getTemplateId(), createdTemplate.getTemplateId());
        assertEquals(testTemplate.getTemplatestats().get(0), testStat);

    }

    @Test
    public void createTemplate_invalidInputs_Stat_with_Statvalue_throwsException() {
        // given
        assertNull(templateRepository.findByTemplateId(1L));


        Stat testStat = new Stat();
        testStat.setStatvalue(200.0);
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);


        Template testTemplate = new Template();


        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);


        // when

        assertThrows(ResponseStatusException.class, () -> templateService.createTemplate(testTemplate));


    }

    @Test
    public void createTemplate_invalidStatsInputs_wrong_ValuesTypes_throwsException() {
        // given
        assertNull(templateRepository.findByTemplateId(1L));


        Stat testStat = new Stat();
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);
        testStat.setValuestypes("KMH");

        Template testTemplate = new Template();


        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);

        assertThrows(ResponseStatusException.class, () -> templateService.createTemplate(testTemplate));
        // when



    }


}


