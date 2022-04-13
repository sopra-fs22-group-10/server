package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.constant.ValuesTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class StatServiceIntegrationTest {

    @Qualifier("statsRepository")
    @Autowired
    private StatRepository statRepository;

    @Autowired
    private StatService statService;



    @BeforeEach
    public void setup() {
        statRepository.deleteAll();
    }

    @AfterEach
    public void teardown(){statRepository.deleteAll();}

    @Test
    public void createStats_validInputs_success() {
        // given
        assertNull(statRepository.findByStatId(1L));


        Stat testStat = new Stat();
        testStat.setStatvalue("200");
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);


        // when
        Stat createdStat = statService.createStat(testStat);


        // then
        assertEquals(testStat.getStatId(), createdStat.getStatId());
        assertEquals(testStat.getStatname(), createdStat.getStatname());
        assertEquals(testStat.getStattype(), createdStat.getStattype());
        assertEquals(testStat.getStatvalue(), createdStat.getStatvalue());

    }




    @Test
    public void getStatById() {
        // given
        assertNull(statRepository.findByStatId(1L));


        Stat testStat = new Stat();
        testStat.setStatvalue("200");
        testStat.setStatId(1L);
        testStat.setStatname("testStat1");
        testStat.setStattype(StatTypes.NUMBER);

        testStat = statService.createStat(testStat);
        // when
        Stat recoveredStat = statService.getStatById(testStat.getStatId());

        // then
        assertEquals(testStat.getStatId(), recoveredStat.getStatId());
        assertEquals(testStat.getStatname(), recoveredStat.getStatname());
        assertEquals(testStat.getStattype(), recoveredStat.getStattype());
        assertEquals(testStat.getStatvalue(), recoveredStat.getStatvalue());

    }


}