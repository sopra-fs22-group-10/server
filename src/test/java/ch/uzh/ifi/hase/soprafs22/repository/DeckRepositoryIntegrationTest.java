package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
public class DeckRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("deckRepository")
    @Autowired
    private DeckRepository deckRepository;

    @Test
    public void findByDeckStatus_success() {
        // given

        Deck testDeck = new Deck();
        testDeck.setDeckname("TestDeck1");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);

        entityManager.persist(testDeck);

        entityManager.flush();


        Deck testDeck2 = new Deck();
        testDeck2.setDeckname("TestDeck2");
        testDeck2.setDeckstatus(DeckStatus.PRIVATE);


        entityManager.persist(testDeck2);
        entityManager.flush();





        // when
        List <Deck> foundPUBLIC = deckRepository.findBydeckstatus(testDeck.getDeckstatus());
        List <Deck> foundPRIVATE = deckRepository.findBydeckstatus(testDeck2.getDeckstatus());

        // then
        assertNotNull(foundPUBLIC.get(0).getDeckId());
        assertEquals(foundPUBLIC.get(0).getDeckId(), testDeck.getDeckId());
        assertEquals(foundPUBLIC.get(0).getDeckname(), testDeck.getDeckname());
        assertEquals(foundPUBLIC.get(0).getDeckstatus(), testDeck.getDeckstatus());


        assertNotNull(foundPRIVATE.get(0).getDeckId());
        assertEquals(foundPRIVATE.get(0).getDeckId(), testDeck2.getDeckId());
        assertEquals(foundPRIVATE.get(0).getDeckname(), testDeck2.getDeckname());
        assertEquals(foundPRIVATE.get(0).getDeckstatus(), testDeck2.getDeckstatus());



    }

    @Test
    public void findMultipleByDeckStatus_success() {
        // given
        deckRepository.deleteAll();
        Deck testDeck = new Deck();

        testDeck.setDeckname("TestDeck1");
        testDeck.setDeckstatus(DeckStatus.PUBLIC);

        entityManager.persist(testDeck);
        entityManager.flush();

        Deck testDeck2 = new Deck();

        testDeck2.setDeckname("TestDeck2");
        testDeck2.setDeckstatus(DeckStatus.PUBLIC);


        entityManager.persist(testDeck2);
        entityManager.flush();

        Deck testDeck3 = new Deck();

        testDeck3.setDeckname("TestDeck3");
        testDeck3.setDeckstatus(DeckStatus.PUBLIC);

        entityManager.persist(testDeck3);
        entityManager.flush();



        // when
        List <Deck> foundPUBLIC = deckRepository.findBydeckstatus(DeckStatus.PUBLIC);

        // then
        assertEquals(foundPUBLIC.size(), 3);
        assertNotNull(foundPUBLIC.get(0).getDeckId());
        assertEquals(foundPUBLIC.get(0).getDeckId(), testDeck.getDeckId());
        assertEquals(foundPUBLIC.get(1).getDeckId(), testDeck2.getDeckId());
        assertEquals(foundPUBLIC.get(2).getDeckId(), testDeck3.getDeckId());



    }




}



