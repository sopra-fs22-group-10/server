package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class SessionPostDTO {

    private String hostUsername;
    private Long hostId;
    private int maxPlayers;
    private Long deckId;
    private Integer deckaccesscode;


    public String getHostUsername() { return hostUsername; }

    public void setHostUsername(String username) { this.hostUsername = username; }

    public Long getHostId() { return hostId; }

    public void setHostId(Long hostId) {this.hostId = hostId; }

    public Long getDeckId() { return deckId; }

    public void setDeckId(Long deckId) {this.deckId = deckId; }

    public int getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public Integer getDeckaccesscode() {
        return deckaccesscode;
    }

    public void setDeckaccesscode(Integer deckaccesscode) {
        this.deckaccesscode = deckaccesscode;
    }
}
