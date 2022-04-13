package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Template;

import javax.persistence.*;
import java.util.List;

public class DeckGetDTO {
    private Long deckId;

    private List<Card> cardlist;

    private String deckname;

    private DeckStatus deckstatus;

    private Template template;



    public List<Card> getCardlist() {
        return cardlist;
    }


    public void addCard(Card card){
        this.cardlist.add(card);
    }

    public void removeCard(Card card){
        this.cardlist.remove(card);
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public String getDeckname() {
        return deckname;
    }

    public void setDeckname(String deckname) {
        this.deckname = deckname;
    }





    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    public DeckStatus getDeckStatus() {
        return deckstatus;
    }

    public void setDeckStatus(DeckStatus status) {
        this.deckstatus = status;
    }



}
