package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Player;

import java.util.List;

public class GamePutDTO {

    private Long opponentPlayer;
    private String currentStatName;



    public void setOpponentPlayer(Long opponentPlayer){ this.opponentPlayer = opponentPlayer; }

    public Long getOpponentPlayer() {return opponentPlayer;}

    public String getCurrentStatName() { return currentStatName;   }

    public void setCurrentStatName(String currentStatName) { this.currentStatName = currentStatName;  }

}
