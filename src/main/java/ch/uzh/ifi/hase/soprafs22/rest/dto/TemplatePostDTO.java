package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Stats;

import java.util.List;

public class TemplatePostDTO {

    private Long templateid;
    private List<Stats> templatestats;
    private int statcount;
    private String templatename;

    public Long getTemplateid() {
        return templateid;
    }

    public void setTemplateid(Long templateid) {
        this.templateid = templateid;
    }

    public List<Stats> getTemplatestats() {
        return templatestats;
    }

    public void setTemplatestats( List<Stats> templatestats){

        this.templatestats=templatestats;

    }

    public int getStatcount() {
        return statcount;
    }

    public void setStatcount(int statcount) {
        this.statcount = statcount;
    }

    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }
}
