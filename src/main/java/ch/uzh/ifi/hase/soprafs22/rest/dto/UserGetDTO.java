package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class UserGetDTO {

    private Long id;
    private String username;
    private UserStatus status;
    private String token;
    private String birthdate;
    private String creationDate;

    public void setCreationDate(LocalDateTime creationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String date = formatter.format(creationDate);
        this.creationDate = date;
    }

    public String getCreationDate() {


        return this.creationDate;
    }

    public String getBirthdate() {
        return this.birthdate;

    }

    public void setBirthdate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.birthdate = dateFormat.format(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }


    public void setStatus(UserStatus status) {
        this.status = status;

    }

}
