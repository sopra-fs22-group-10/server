package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Stat;

import java.util.List;

public class TemplatePostDTO {

    //private Long templateid;
    private List<Stat> templatestats;
    private int statcount;

    /*
    public Long getTemplateid() {
        return templateid;
    }

    public void setTemplateid(Long templateid) {
        this.templateid = templateid;
    }

     */

    public List<Stat> getTemplatestats() {
        return templatestats;
    }

    public void setTemplatestats( List<Stat> templatestats){

        this.templatestats=templatestats;

    }

    public int getStatcount() {
        return statcount;
    }

    public void setStatcount(int statcount) {
        this.statcount = statcount;
    }

}
