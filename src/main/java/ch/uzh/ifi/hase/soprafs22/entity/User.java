package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "USER")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    @Column
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UserStatus status;

    @Column
    private LocalDate birthdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate){this.creationDate = creationDate; }

    public LocalDate getBirthdate(){return this.birthdate;}

    public void setBirthdate(LocalDate birthdate){this.birthdate = birthdate; }

    public LocalDateTime getCreationDate(){return this.creationDate;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}