package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.constant.DeckStatus;


@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long deckId;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Card> cardlist;

    //the Deck name doesn't have to be unique since User can get public Decks and Decks from other people
    @Column(nullable = false)
    private String deckname;


    //Deck private or public
    @Column(nullable = false)
    private DeckStatus deckstatus;


    @ManyToOne
    private Template template;

    /*public List getCards() {
        return cardlist;
    }

     */

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


    public DeckStatus getDeckStatus() {
        return deckstatus;
    }

    public void setDeckStatus(DeckStatus deckstatus) {
        this.deckstatus = deckstatus;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }
}

