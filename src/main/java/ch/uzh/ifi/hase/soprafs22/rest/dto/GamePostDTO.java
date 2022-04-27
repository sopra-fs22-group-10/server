package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Player;

import java.util.List;

public class GamePostDTO {


    private Long gameCode;

    public void setGameCode(Long gameCode) {this.gameCode = gameCode; }

    public Long getGameCode() { return gameCode; }

}
