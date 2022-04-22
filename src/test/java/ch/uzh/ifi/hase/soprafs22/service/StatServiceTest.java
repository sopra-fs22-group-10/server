package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StatServiceTest {

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private StatService statService;

    private Stat testStat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testStat = new Stat();
        testStat.setStatId(1L);
        testStat.setStatname("testStatname");
        testStat.setStatvalue("200");
        testStat.setStattype(StatTypes.NUMBER);

        // when -> any object is being saved in the statRepository -> return the dummy
        // testStat
        Optional<Stat> optionalStat = Optional.of(testStat);
        Mockito.when(statRepository.findById(Mockito.any())).thenReturn(optionalStat);
        Mockito.when(statRepository.save(Mockito.any())).thenReturn(testStat);
        Mockito.when(statRepository.save(Mockito.any())).thenReturn(testStat);
    }

    @Test
    public void createStats_validInputs_success() {
        // when -> any object is being saved in the statRepository -> return the dummy
        // testStat
        Stat createdStat = statService.createStat(testStat);

        // then
        Mockito.verify(statRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testStat.getStatId(), createdStat.getStatId());
        assertEquals(testStat.getStatname(), createdStat.getStatname());
        assertEquals(testStat.getStatvalue(), createdStat.getStatvalue());
        assertEquals(testStat.getStattype(), createdStat.getStattype());
    }

    @Test
    public void getStatById() {
        // given

        Mockito.when(statRepository.findByStatId(Mockito.any())).thenReturn(testStat);

        // when
        Stat recoveredStat = statService.getStatById(testStat.getStatId());

        // then
        assertEquals(testStat.getStatId(), recoveredStat.getStatId());
        assertEquals(testStat.getStatname(), recoveredStat.getStatname());
        assertEquals(testStat.getStattype(), recoveredStat.getStattype());
        assertEquals(testStat.getStatvalue(), recoveredStat.getStatvalue());

    }



}