package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Stat;

import java.util.List;

public class CardPutDTO {
    private String cardname;
    private String image;
    private List<Stat> cardstats;
    private Long cardId;


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
