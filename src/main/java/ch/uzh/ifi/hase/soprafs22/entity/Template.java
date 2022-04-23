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


    @OneToMany
    private List<Stat> templatestats;



    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateid) {
        this.templateId = templateid;
    }

    //Maybe needs to raise an error
    //When stat is added the is increased aswell
    /*
    public void addStats(Stats stat){

        templatestats.add(stat);
    }

    //Maybe needs to raise an error if you remove stat whe is zero
    public void deleteStats(Stats stat){

        templatestats.remove(stat);
    }

     */
    public void setTemplatestats( List<Stat> templatestats){
        this.templatestats=templatestats;
    }

    public List<Stat> getTemplatestats() {
        return templatestats;
    }


}



