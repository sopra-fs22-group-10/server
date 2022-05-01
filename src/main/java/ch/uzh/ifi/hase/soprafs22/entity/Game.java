package ch.uzh.ifi.hase.soprafs22.entity;


import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs22.constant.RoundStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column
    private Long gameCode;

    @Column
    private Long currentPlayer;

    @Column
    private Long opponentPlayer;

    //The game Entity should have a OneToMany mapping. So multiple Players per game and just one game per player
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Player> playerList;

    @Column
    private String currentStatName;

    @Column
    private RoundStatus roundStatus;

    @Column
    private Long winner;


    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long deckId) {
        this.gameId = deckId;
    }

    public Long getGameCode() {
        return gameCode;
    }

    public void setGameCode(Long gameCode) {
        this.gameCode = gameCode;
    }

    public Long getOpponentPlayer() {
        return opponentPlayer;
    }

    public void setOpponentPlayer(Long opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public Long getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Long currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Player> getPlayerList() {return playerList; }

    public void setPlayerList(List<Player> playerList) {this.playerList = playerList; }

    public String getCurrentStatName() {
        return currentStatName;
    }

    public void setCurrentStatName(String currentStatName) {
        this.currentStatName = currentStatName;
    }

    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(RoundStatus playerStatus) {
        this.roundStatus = playerStatus;
    }

    public Long getWinner() {
        return winner;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }
}

