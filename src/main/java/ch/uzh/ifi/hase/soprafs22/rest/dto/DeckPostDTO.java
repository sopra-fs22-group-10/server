package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class DeckPostDTO {
    private String deckname;
    private String deckImage;
    private String deckstatus;

    public String getDeckname() {
        return deckname;
    }
    public String getDeckImage() {
        return deckImage;
    }
    public String getDeckstatus() {
        return deckstatus;
    }

    public void setDeckname(String deckname) {
        this.deckname = deckname;
    }
    public void setDeckImage(String deckImage) {
        this.deckImage = deckImage;
    }
    public void setDeckstatus(String deckstatus) {
        this.deckstatus = deckstatus;
    }
}
