package ch.uzh.ifi.hase.soprafs22.entity;



import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Column
    private Integer deckaccesscode;

    @Column
    private String deckImage;


    //Right now Template has many to one Relationship but could be changed to One to One
    @ManyToOne
    private Template template;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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
        if(deckstatus == DeckStatus.PRIVATE){
            Random random = new Random();
            setDeckaccesscode(random.nextInt(10000));
        }
        else{
            if(getDeckaccesscode() != null){
                setDeckaccesscode(null);
            }

        }
        this.deckstatus = deckstatus;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    public Integer getDeckaccesscode() {
        return deckaccesscode;
    }

    public void setDeckaccesscode(Integer deckacesscode) {
        this.deckaccesscode = deckacesscode;
    }

    public String getDeckImage() {
        return deckImage;
    }

    public void setDeckImage(String deckImage) {
        this.deckImage = deckImage;
    }
}

