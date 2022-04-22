package ch.uzh.ifi.hase.soprafs22.entity;

import org.springframework.data.annotation.Reference;

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

    @Column(nullable = false)
    private String cardname;

    @Column(nullable = false)
    private String image;

    @OneToMany(fetch = FetchType.EAGER)
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


    public void setCardstats(List<Stat> cardstats) {
        this.cardstats = cardstats;
    }

    public List<Stat> getCardstats() {
        return cardstats;
    }
}


