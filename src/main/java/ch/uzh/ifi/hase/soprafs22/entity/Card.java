package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "CARD")
public class Card implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long cardId;

    //Not sure if necessary
    //@ManyToOne
    //@JoinColumn(name = "deck_id")
    //private Deck deck;

    @Column(nullable = false, unique = true)
    private String cardname;

    @Column(nullable = false)
    private String image;

    @OneToMany
    private List<Stat> cardstats;

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long id) {
        this.cardId = id;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public void addStat(Stat stat){
        this.cardstats.add(stat);
    }

    public void setCardstats(List<Stat> cardstats) {
        this.cardstats = cardstats;
    }

    public List<Stat> getCardstats() {
        return cardstats;
    }
}


