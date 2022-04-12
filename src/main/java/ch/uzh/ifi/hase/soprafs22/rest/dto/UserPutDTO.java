package ch.uzh.ifi.hase.soprafs22.rest.dto;


public class UserPutDTO {


    private String username;

    private String token;
    private String birthdate;
    private Long id;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBirthdate(){return birthdate;}

    public void setBirthdate(String birthdate){this.birthdate = birthdate; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }


}

