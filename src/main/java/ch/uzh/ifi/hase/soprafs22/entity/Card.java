package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;

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
    private Long id;

    //Not sure if necessary
    //@ManyToOne
    //@JoinColumn(name = "deck_id")
    //private Deck deck;

    @Column(nullable = false, unique = true)
    private String cardname;

    @Column(nullable = false)
    private String image;

    @OneToMany
    private List<Stats> cardstats;

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public void addStat(Stats stat){
        this.cardstats.add(stat);
    }


}


