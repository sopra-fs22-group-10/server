package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;

import java.util.List;

public class UserGetDTO {

    private Long userId;
    private String username;
    private UserStatus status;
    private List<Deck> deckList;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public UserStatus getStatus() {return status;}

    public void setStatus(UserStatus status) {this.status = status;}

    public void setDeckList(List<Deck> deckList) {
        this.deckList = deckList;
    }

    public List<Deck> getDeckList() {
        return deckList;
    }
}
