package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class DeckPutDTO {
    Long deckId;
    String deckAccessCode;

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long userId) {
        this.deckId = userId;
    }

    public String getDeckAccessCode() {
        return deckAccessCode;
    }

    public void setDeckAccessCode(String deckAccessCode) {
        this.deckAccessCode = deckAccessCode;
    }
}
