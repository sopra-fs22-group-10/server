package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


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
        Mockito.when(statRepository.save(Mockito.any())).thenReturn(testStat);
    }

    @Test
    public void createStat_success() {
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
    //Removed duplicate name/password check since passwords should not throw an exception if they are not unique

}