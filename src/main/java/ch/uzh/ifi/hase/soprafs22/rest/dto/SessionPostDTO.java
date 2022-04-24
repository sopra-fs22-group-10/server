package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class SessionPostDTO {

    private String hostUsername;
    private int maxPlayers;
    private Long deckId;


    public String getHostUsername() { return hostUsername; }

    public void setHostUsername(String username) { this.hostUsername = username; }

    public Long getDeckId() { return deckId; }

    public void setDeckId(Long deckId) {this.deckId = deckId; }

    public int getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
}
