package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class SessionPostDTO {

    private String username;
    private int maxPlayers;
    private Long deckId;


    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public Long getDeckId() { return deckId; }

    public void setDeckId(Long deckId) {this.deckId = deckId; }

    public int getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
}
