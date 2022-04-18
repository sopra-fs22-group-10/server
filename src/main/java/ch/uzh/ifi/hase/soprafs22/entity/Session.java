package ch.uzh.ifi.hase.soprafs22.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

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
@Table(name = "SESSION")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long sessionId;

    @Column(nullable = false)
    private String hostUsername;

    @Column(nullable = false)
    private int gameCode;

    @Column(nullable = false)
    private int maxPlayers;

    @Column(nullable = false)
    private Long deckId;

    @ElementCollection
    private List<String> userList;


    public void setSessionId(Long sessionId) {this.sessionId = sessionId; }

    public Long getSessionId() { return sessionId;    }

    public void setHostUsername(String hostUsername) {this.hostUsername = hostUsername; }

    public String getHostUsername() { return hostUsername; }

    public void setGameCode(int gameCode) {this.gameCode = gameCode; }

    public int getGameCode() { return gameCode;    }

    public void setMaxPlayers(int maxPlayers) {this.maxPlayers = maxPlayers; }

    public int getMaxPlayers() { return maxPlayers;    }

    public void setDeckId(Long deckId) {this.deckId = deckId; }

    public Long getDeckId() { return deckId;    }

    public void addUser(String user) { this.userList.add(user); }

    public void removeUser(String user) { this.userList.remove(user); }

    public void setUserList(List<String> userList) {this.userList = userList; }

    public List<String> getUserList() { return userList; }
}
