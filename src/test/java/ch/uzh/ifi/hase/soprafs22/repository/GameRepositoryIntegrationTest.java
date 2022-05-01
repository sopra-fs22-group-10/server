package ch.uzh.ifi.hase.soprafs22.repository;


import ch.uzh.ifi.hase.soprafs22.entity.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void findByGameCode() {
        // given

        Game game = new Game();
        game.setGameCode(1L);
        //game.setGameId(1L);
        game.setCurrentStatName(null);
        game.setRoundStatus(null);
        game.setPlayerList(new ArrayList<>());
        game.setWinner(null);
        game.setCurrentPlayer(1L);
        game.setOpponentPlayer(null);


        entityManager.persist(game);
        entityManager.flush();

        // when
        Game found = gameRepository.findByGameCode(game.getGameCode());

        // then
        assertNotNull(found.getGameCode());
        assertEquals(found.getCurrentPlayer(), game.getCurrentPlayer());
        assertEquals(found.getOpponentPlayer(), game.getOpponentPlayer());
        assertEquals(found.getPlayerList(), game.getPlayerList());
        assertEquals(found.getRoundStatus(), game.getRoundStatus());
        assertEquals(found.getCurrentStatName(), game.getCurrentStatName());
        assertEquals(found.getWinner(), game.getWinner());
    }
}