package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class SessionGetDTO {

    private Long sessionId;
    private int gameCode;

    public Long getSessionId() { return sessionId; }

    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public int getGameCode() { return gameCode; }

    public void setGameCode(int gameCode) { this.gameCode = gameCode; }
}
