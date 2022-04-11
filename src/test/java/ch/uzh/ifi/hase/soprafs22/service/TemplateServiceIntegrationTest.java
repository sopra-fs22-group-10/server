package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.constant.ValuesTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class TemplateServiceIntegrationTest {

    @Qualifier("templateRepository")
    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private StatService statService;



    @BeforeEach
    public void setup() {
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



        testTemplate.setTemplatename("TemplateName1");

        List<Stat> templateStats = new ArrayList<Stat>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setStatcount(1);


        // when
        Template createdTemplate = templateService.createTemplate(testTemplate);

        // then
        assertEquals(testTemplate.getTemplateId(), createdTemplate.getTemplateId());
        assertEquals(testTemplate.getTemplatename(), createdTemplate.getTemplatename());
        assertEquals(testTemplate.getTemplatestats().get(0), testStat);
        assertEquals(testTemplate.getTemplatename(), createdTemplate.getTemplatename());

    }

    @Test
    public void createTemplate_invalidInputs_Stat_with_Statvalue_throwsException() {
        // given
        assertNull(templateRepository.findByTemplateId(1L));


        Stat testStat = new Stat();
        testStat.setStatvalue("200");
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);


        Template testTemplate = new Template();
        testTemplate.setTemplatename("TemplateName1");

        List<Stat> templateStats = new ArrayList<Stat>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setStatcount(1);


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
        testStat.setValuestypes(ValuesTypes.KMH);

        Template testTemplate = new Template();
        testTemplate.setTemplatename("TemplateName1");

        List<Stat> templateStats = new ArrayList<Stat>();
        templateStats.add(testStat);
        testTemplate.setTemplatestats(templateStats);
        testTemplate.setStatcount(1);

        assertThrows(ResponseStatusException.class, () -> templateService.createTemplate(testTemplate));
        // when



    }


}


