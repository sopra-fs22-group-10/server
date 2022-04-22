package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Stat;

import java.util.List;

public class CardPostDTO {

    private String cardname;
    private String image;
    private List<Stat> cardstats;



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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
