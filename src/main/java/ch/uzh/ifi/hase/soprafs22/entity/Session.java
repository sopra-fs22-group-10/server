package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SESSION")
public class Session implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    private Long sessionId;

    @Column(nullable = false)
    private Long deckId;

    @Column(nullable = false)
    private String authentication;

    @Column(nullable = false)
    private int maxPlayers;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int gameCode;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getDeckId() { return deckId; }

    public void setDeckId(Long deckId) { this.deckId = deckId; }

    public String getAuthentication() { return authentication;  }

    public void setAuthentication(String authentication) { this.authentication = authentication; }

    public int getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public int getGameCode() { return gameCode; }

    public void setGameCode(int gameCode) { this.gameCode = gameCode; }
}

