package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Player;

import java.util.List;

public class RoundGetDTO {

    private String currentStatName;
    private RoundStatus roundStatus;


    public String getCurrentStatName() { return currentStatName; }

    public void setCurrentStatName(String currentStatName) { this.currentStatName = currentStatName; }

    public RoundStatus getRoundStatus() { return roundStatus;}

    public void setRoundStatus(RoundStatus roundStatus) { this.roundStatus = roundStatus; }


}
