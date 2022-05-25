package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.util.List;

public class SessionGetDTO {


    private int gameCode;
    private String hostUsername;
    private Long hostId;
    private List<String> userList;
    private int maxPlayers;
    private Long deckId;
    private Integer deckaccesscode;
    private Boolean hasGame;
    private Integer deckCode;

    public int getGameCode() { return gameCode; }

    public void setGameCode(int gameCode) { this.gameCode = gameCode; }

    public String getHostUsername() { return hostUsername; }

    public void setHostUsername(String hostUsername) {this.hostUsername = hostUsername; }

    public Long getHostId() { return hostId; }

    public void setHostId(Long hostId) {this.hostId = hostId; }

    public List<String> getUserList() { return userList; }

    public void setUserList(List<String> userList)  { this.userList = userList; }

    public int getMaxPlayers() {return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public Long getDeckId() { return deckId;   }

    public void setDeckId(Long deckId) {this.deckId = deckId; }

    public Boolean getHasGame() {
        return hasGame;
    }

    public void setHasGame(Boolean hasGame) {
        this.hasGame = hasGame;
    }


    public Integer getDeckaccesscode() {
        return deckaccesscode;
    }

    public void setDeckaccesscode(Integer deckaccesscode) {
        this.deckaccesscode = deckaccesscode;
    }
}
