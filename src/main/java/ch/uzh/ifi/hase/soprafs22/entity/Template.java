package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;




@Entity
@Table(name = "TEMPLATE")
public class Template implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long templateId;


    @Column(nullable = false)
    private int statcount;

    @OneToMany
    private List<Stat> templatestats;



    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateid) {
        this.templateId = templateid;
    }

    //Maybe needs to raise an error
    //When stat is added the statcount is increased aswell
    /*
    public void addStats(Stats stat){
        statcount += 1;
        templatestats.add(stat);
    }

    //Maybe needs to raise an error if you remove stat when statcount is zero
    public void deleteStats(Stats stat){
        statcount -=1;
        templatestats.remove(stat);
    }

     */
    public void setTemplatestats( List<Stat> templatestats){
        this.templatestats=templatestats;
        this.statcount = templatestats.size();
    }

    public List<Stat> getTemplatestats() {
        return templatestats;
    }



    public int getStatcount() {
        return statcount;
    }

    public void setStatcount(int statcount) {
        this.statcount = statcount;
    }



}



