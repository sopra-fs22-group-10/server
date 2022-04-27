package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class JoinSessionPostDTO {

    private String username;

    private Long userId;


    public void setUsername(String username) { this.username = username; }

    public String getUsername(){ return username; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getUserId(){ return userId; }
}
