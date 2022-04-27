package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.PlayerStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */

@Entity
@Table(name = "PLAYER ")
public class Player implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long playerId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private PlayerStatus playerStatus;

    @Column(nullable = false)
    private ArrayList<LinkedList> cards;



    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {this.playerId = playerId;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public ArrayList<LinkedList> getCards() {
        return cards;
    }

    public void setCards(ArrayList<LinkedList> cards) { this.cards = cards;  }

    public void addList(LinkedList<Card> newList){
        if(cards == null){
            cards = new ArrayList<>();
        }
        this.cards.add(newList);
    }

}

