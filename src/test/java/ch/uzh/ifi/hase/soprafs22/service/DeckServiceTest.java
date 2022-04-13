package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private DeckService deckService;

    private Deck testDeck;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testDeck = new Deck();
        testDeck.setDeckId(1L);
        testDeck.setDeckstatus(DeckStatus.PUBLIC);
        testDeck.setDeckname("testDeckname");

        // when -> any object is being saved in the deckRepository -> return the dummy
        // testDeck
        Mockito.when(deckRepository.save(Mockito.any())).thenReturn(testDeck);
    }

    @Test
    public void createDeck_success() {
        // when -> any object is being saved in the deckRepository -> return the dummy
        // testDeck
        Deck createdDeck = deckService.createDeck(testDeck);

        // then
        Mockito.verify(deckRepository, Mockito.times(2)).save(Mockito.any());

        assertEquals(testDeck.getDeckId(), createdDeck.getDeckId());
        assertEquals(testDeck.getDeckname(), createdDeck.getDeckname());
        assertEquals(DeckStatus.PUBLIC, createdDeck.getDeckstatus());
    }

    @Test
    public void get_Deck_by_Id(){

        Deck createdDeck = deckService.getDeckById(testDeck.getDeckId());

        assertEquals(testDeck.getDeckId(), createdDeck.getDeckId());
        assertEquals(testDeck.getDeckname(), createdDeck.getDeckname());
        assertEquals(DeckStatus.PUBLIC, createdDeck.getDeckstatus());
    }


    @Test
    public void get_not_existing_Deck_by_Id(){

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> deckService.getDeckById(2L));
        assertEquals(thrown.getStatus(), HttpStatus.NOT_FOUND);
    }


    //Removed duplicate name/password check since passwords should not throw an exception if they are not unique

}

