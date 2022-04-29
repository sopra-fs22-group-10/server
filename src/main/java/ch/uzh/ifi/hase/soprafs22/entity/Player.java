package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column
    private Long playerId;

    @Column
    private String playerName;

    @Column
    private PlayerStatus playerStatus;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Card> hand;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Card> playedCards;
    /*
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "game_game_id")
    private Game game;
*/

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(List<Card> playedCards) {
        this.playedCards = playedCards;
    }
/*
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }*/
}


