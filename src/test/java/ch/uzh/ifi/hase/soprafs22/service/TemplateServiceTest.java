package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import ch.uzh.ifi.hase.soprafs22.repository.TemplateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private StatRepository statRepository;

    @Mock
    private StatService statService;

    @InjectMocks
    private TemplateService templateService;


    private Template testTemplate;
    private Stat testStat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testTemplate = new Template();
        testTemplate.setTemplateId(1L);
        testTemplate.setStatcount(0);


        testStat = new Stat();
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);


        // when -> any object is being saved in the templateRepository -> return the dummy
        // testTemplate
        Mockito.when(templateRepository.save(Mockito.any())).thenReturn(testTemplate);

        Mockito.when(statService.createStat(Mockito.any())).thenReturn(testStat);
    }

    @AfterEach
    public void teardown() {
        statRepository.deleteAll();
        templateRepository.deleteAll();
    }


    @Test
    public void createTemplate_success() {
        // when -> any object is being saved in the templateRepository -> return the dummy
        // testTemplate
        Mockito.when(statRepository.save(Mockito.any())).thenReturn(testStat);
        Template createdTemplate = templateService.createTemplate(testTemplate);


        // then
        Mockito.verify(templateRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testTemplate.getTemplateId(), createdTemplate.getTemplateId());
        assertEquals(testTemplate.getStatcount(), createdTemplate.getStatcount());
        assertEquals(testTemplate.getTemplatestats(), createdTemplate.getTemplatestats());
    }

    @Test
    public void createTemplate_invalidInputs_Stat_with_Statvalue_throwsException() {
        // given


        Stat someStat = new Stat();
        someStat.setStatvalue("200");
        someStat.setStatname("testStat1");
        someStat.setStattype(StatTypes.NUMBER);


        Mockito.when(statService.createStat(Mockito.any())).thenReturn(someStat);

        Template testTemplate = new Template();


        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(someStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setStatcount(1);


        // when

        assertThrows(ResponseStatusException.class, () -> templateService.createTemplate(testTemplate));


    }
}

