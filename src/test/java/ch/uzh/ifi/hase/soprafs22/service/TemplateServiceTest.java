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
        testTemplate.setTemplatename("testTemplatename");

        testStat = new Stat();
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);
        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);



        // when -> any object is being saved in the templateRepository -> return the dummy
        // testTemplate
        Mockito.when(templateRepository.save(Mockito.any())).thenReturn(testTemplate);
        Mockito.when(statRepository.save(Mockito.any())).thenReturn(testStat);
        Mockito.when(statService.createStat(Mockito.any())).thenReturn(testStat);
    }

    @AfterEach
    public void teardown(){
        statRepository.deleteAll();
        templateRepository.deleteAll();
    }


    @Test
    public void createTemplate_success() {
        // when -> any object is being saved in the templateRepository -> return the dummy
        // testTemplate

        Template createdTemplate = templateService.createTemplate(testTemplate);


        // then
        Mockito.verify(templateRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testTemplate.getTemplateId(), createdTemplate.getTemplateId());
        assertEquals(testTemplate.getTemplatename(), createdTemplate.getTemplatename());
        assertEquals(testTemplate.getStatcount(), createdTemplate.getStatcount());
    }
    //Removed duplicate name/password check since passwords should not throw an exception if they are not unique



}
