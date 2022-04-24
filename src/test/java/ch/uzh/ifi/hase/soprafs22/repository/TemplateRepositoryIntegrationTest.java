package ch.uzh.ifi.hase.soprafs22.repository;


import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TemplateRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TemplateRepository templateRepository;

    @Test
    public void findById_success() {
        // given



        Stat testStat = new Stat();
        testStat.setStatvalue("200");
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);

        entityManager.persist(testStat);
        entityManager.flush();

        Template template = new Template();


        List<Stat> templateStats = new ArrayList<>();
        templateStats.add(testStat);
        template.setTemplatestats(templateStats);

        // given
        entityManager.persist(template);
        entityManager.flush();

        // when
        Template found = templateRepository.findByTemplateId(template.getTemplateId());

        // then
        assertNotNull(found.getTemplateId());
        assertEquals(found.getTemplatestats().get(0), testStat);
        assertEquals(found.getTemplatestats().size(), templateStats.size());

    }




}
