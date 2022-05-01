package ch.uzh.ifi.hase.soprafs22.repository;


import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PlayerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void findByPlayerId() {
        // given
        Player player = new Player();
        player.setPlayerId(1L);
        player.setPlayerStatus(PlayerStatus.ACTIVE);
        player.setPlayerName("player");
        player.setHand(new ArrayList<>());
        player.setPlayedCards(new ArrayList<>());



        entityManager.persist(player);
        entityManager.flush();

        // when
        Player found = playerRepository.findByPlayerId(player.getPlayerId());

        // then
        assertNotNull(found.getPlayerId());
        assertEquals(found.getPlayerName(), player.getPlayerName());
        assertEquals(found.getPlayerId(), player.getPlayerId());
        assertEquals(found.getPlayerStatus(), player.getPlayerStatus());
        assertEquals(found.getHand(), player.getHand());
        assertEquals(found.getPlayedCards(), player.getPlayedCards());

    }
}