package ch.uzh.ifi.hase.soprafs22.entity;


import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.constant.ValuesTypes;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "STAT")
public class Stat implements Serializable {

    @Id
    @GeneratedValue
    private Long statId;

    @Column
    private String statvalue;

    @Column(nullable = false)
    private String statname;

    @Column(nullable = false)
    private StatTypes stattype;

    @Column
    private ValuesTypes valuestypes;

    public void setStatvalue(String statvalue) {
        this.statvalue = statvalue;
    }

    public String getStatvalue() {
        return statvalue;
    }

    public void setStatname(String statname) {
        this.statname = statname;
    }

    public String getStatname() {
        return statname;
    }

    public void setStattype(StatTypes stattype) {
        this.stattype = stattype;
    }

    public StatTypes getStattype() {
        return stattype;
    }

    public void setValuestypes(ValuesTypes valuestypes) {
        this.valuestypes = valuestypes;
    }

    public ValuesTypes getValuestypes() {
        return valuestypes;
    }

    public Long getStatId() {
        return statId;
    }

    public void setStatId(Long id) {
        this.statId = id;
    }




}
