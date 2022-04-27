package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class DeckPutDTO {
    private Long deckId;
    private Integer deckAccessCode;

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public Integer getDeckAccessCode() {
        return deckAccessCode;
    }

    public void setDeckAccessCode(Integer deckAccessCode) {
        this.deckAccessCode = deckAccessCode;
    }
}
