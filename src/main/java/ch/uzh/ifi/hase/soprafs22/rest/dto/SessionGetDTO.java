package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.util.List;

public class SessionGetDTO {

    private int gameCode;
    private String hostUsername;
    private List<String> userList;
    private int maxPlayers;
    private Long deckId;


    public int getGameCode() { return gameCode; }

    public void setGameCode(int gameCode) { this.gameCode = gameCode; }

    public String getHostUsername() { return hostUsername; }

    public void setHostUsername(String hostUsername) {this.hostUsername = hostUsername; }

    public List<String> getUserList() { return userList; }

    public void setUserList(List<String> userList)  { this.userList = userList; }

    public int getMaxPlayers() {return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public Long getDeckId() { return deckId;   }

    public void setDeckId(Long deckId) {this.deckId = deckId; }


}
