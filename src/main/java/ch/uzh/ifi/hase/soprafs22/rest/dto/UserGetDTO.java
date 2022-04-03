package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import java.time.LocalDate;

public class UserGetDTO {

    private Long id;
    private String username;
    private UserStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public UserStatus getStatus() {return status;}

    public void setStatus(UserStatus status) {this.status = status;}
}
