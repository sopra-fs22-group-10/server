package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Player;

import java.util.List;

public class GameGetDTO {

    private Long currentPlayer;
    private Long opponentPlayer;
    private List<Player> playerList;
    private Long winner;


    public void addPlayer(Player player) { this.playerList.add(player); }

    public void removePlayer(Player player) { this.playerList.remove(player); }

    public void setPlayerList(List<Player> playerList) {this.playerList = playerList; }

    public List<Player> getPlayerList() { return playerList; }

    public void setCurrentPlayer(Long currentPlayer){ this.currentPlayer = currentPlayer; }

    public Long getCurrentPlayer() {return currentPlayer;}

    public void setOpponentPlayer(Long opponentPlayer){ this.opponentPlayer = opponentPlayer; }

    public Long getOpponentPlayer() {return opponentPlayer;}

    public Long getWinner() { return winner;   }

    public void setWinner(Long winner) { this.winner = winner;  }

}
