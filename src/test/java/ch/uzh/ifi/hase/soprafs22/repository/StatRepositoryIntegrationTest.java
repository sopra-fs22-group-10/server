package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class StatRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StatRepository statRepository;

    @Test
    public void findById_success() {
        // given



        Stat testStat = new Stat();
        testStat.setStatvalue("200");
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);

        entityManager.persist(testStat);
        entityManager.flush();


        // given
        entityManager.persist(testStat);
        entityManager.flush();

        // when
        Stat found = statRepository.findByStatId(testStat.getStatId());

        // then
        assertNotNull(found.getStatId());
        assertEquals(found.getStatId(), testStat.getStatId());
        assertEquals(found.getStatname(), testStat.getStatname());
        assertEquals(found.getStatvalue(), testStat.getStatvalue());
        assertEquals(found.getStattype(), testStat.getStattype());


    }




}

