package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;

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
@Table(name = "SESSION")
public class Session implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    private Long sessionId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int gameCode;

    @Column(nullable = false)
    private int maxPlayers;

    @Column(nullable = false)
    private Long deckId;


    public void setSessionId(Long sessionId) {this.sessionId = sessionId; }

    public Long getSessionId() { return sessionId;    }

    public void setUsername(String username) {this.username = username; }

    public String getUsername() { return username; }

    public void setGameCode(int gameCode) {this.gameCode = gameCode; }

    public int getGameCode() { return gameCode;    }

    public void setMaxPlayers(int maxPlayers) {this.maxPlayers = maxPlayers; }

    public int getMaxPlayers() { return maxPlayers;    }

    public void setDeckId(Long deckId) {this.deckId = deckId; }

    public Long getDeckId() { return deckId;    }

}
