package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class DeckServiceIntegrationTest {

    @Qualifier("deckRepository")
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private DeckService deckService;



    @BeforeEach
    public void setup() {
        deckRepository.deleteAll();
    }

    @Test
    public void createDeck_validInputs_success() {
        // given
        assertEquals(Collections.emptyList(), deckRepository.findBydeckstatus(DeckStatus.PUBLIC));


        Deck testDeck = new Deck();


        // when
        Deck createdDeck = deckService.createDeck(testDeck);

        // then
        assertEquals(testDeck.getDeckId(), createdDeck.getDeckId());
        assertEquals("Deck Nr."+testDeck.getDeckId(), createdDeck.getDeckname());
        assertEquals(DeckStatus.PUBLIC, createdDeck.getDeckstatus());
    }


}

