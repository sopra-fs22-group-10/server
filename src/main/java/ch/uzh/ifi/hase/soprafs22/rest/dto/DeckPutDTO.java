package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class DeckPutDTO {
    private Long deckId;
    private String deckAccessCode;

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public String getDeckAccessCode() {
        return deckAccessCode;
    }

    public void setDeckAccessCode(String deckAccessCode) {
        this.deckAccessCode = deckAccessCode;
    }
}
