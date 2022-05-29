package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class DeckPutDTO {

    private String deckImage;
    private String deckstatus;
    private String deckname;


    public String getDeckImage() {
        return deckImage;
    }

    public void setDeckImage(String deckimage) {
        this.deckImage = deckimage;
    }

    public String getDeckstatus() {
        return deckstatus;
    }

    public void setDeckdeckstatus(String deckstatus) {
        this.deckstatus = deckstatus;
    }

    public String getDeckname() {
        return deckname;
    }

    public void setDeckname(String deckname) {
        this.deckname = deckname;
    }
}
