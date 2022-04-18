package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class SessionPostDTO {

    private String hostUsername;
    private Long deckId;
    private int maxPlayers;



    public String getHostUsername() { return hostUsername; }

    public void setHostUsername(String hostUsername) { this.hostUsername = hostUsername; }

    public Long getDeckId() { return deckId; }

    public void setDeckId(Long deckId) {this.deckId = deckId; }

    public int getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
}
