package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.RoundStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Internal Session Representation.
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long gameCode;

    @Column
    private Long currentPlayer;

    @Column
    private Long opponentPlayer;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Player> playerList;

    @Column
    private String currentStatName;

    @Column
    private RoundStatus roundStatus;

    @Column
    private Long winner;



    public void setGameCode(Long gameCode) {this.gameCode = gameCode; }

    public Long getGameCode() { return gameCode;    }

    public void addPlayer(Player player) { this.playerList.add(player); }

    public void removePlayer(Player player) { this.playerList.remove(player); }

    public void setPlayerList(List<Player> playerList) {this.playerList = playerList; }

    public List<Player> getPlayerList() { return playerList; }

    public void setCurrentPlayer(Long currentPlayer){ this.currentPlayer = currentPlayer; }

    public Long getCurrentPlayer() {return currentPlayer;}

    public void setOpponentPlayer(Long opponentPlayer){ this.opponentPlayer = opponentPlayer; }

    public Long getOpponentPlayer() {return opponentPlayer;}

    public void setCurrentStatName(String currentStatName) { this.currentStatName = currentStatName; }

    public String getCurrentStatName() { return currentStatName;  }

    public void setRoundStatus(RoundStatus roundStatus){ this.roundStatus = roundStatus; }

    public RoundStatus getRoundStatus() { return roundStatus; }

    public void setWinner(Long winner){ this.winner = winner; }

    public Long getWinner() { return winner; }
}
