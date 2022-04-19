package ch.uzh.ifi.hase.soprafs22.entity;



import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;


@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long deckId;

    //the Deck name doesn't have to be unique since User can get public Decks and Decks from other people
    @Column(nullable = false)
    private String deckname;

    //Deck private or public
    @Column(nullable = false)
    private DeckStatus deckstatus;

    @ManyToOne
    private Template template;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Card> cardList;


    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void addCard(Card card){
        if(cardList == null || cardList.isEmpty()){
            cardList = new ArrayList<>();
        }
        this.cardList.add(card);
    }

    public void removeCard(Card card){
        this.cardList.remove(card);
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

    public DeckStatus getDeckstatus() {
        return deckstatus;
    }

    public void setDeckstatus(DeckStatus deckstatus) {
        this.deckstatus = deckstatus;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }
}

