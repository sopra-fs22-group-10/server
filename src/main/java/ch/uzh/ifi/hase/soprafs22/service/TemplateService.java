package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.StatTypes;
import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class TemplateService {
    private final Logger log = LoggerFactory.getLogger(TemplateService.class);

    private final TemplateRepository templateRepository;

    private final StatService statService;

    @Autowired
    public TemplateService(@Qualifier("templateRepository") TemplateRepository templateRepository, StatService statService) {
        this.templateRepository = templateRepository;

        this.statService = statService;
    }
    /*
    *Not sure if get Templates is necessary
    *
    *
    public List<Template> getTemplates() {
        return this.templateRepository.findAll();
    }

     */

    //Not sure if change Template is necessary
    /*
    public void changeTemplate(Template newTemplate){

        Template templateToChange = getTemplateById(newTemplate.getTemplateId());
        templateToChange = newTemplate;

        templateRepository.flush();
        log.debug("Updated Information for Template: {}", templateToChange);
    }

     */

    public Template createTemplate(Template newTemplate) {

        // saves the given entity but data is only persisted in the database once
        // flush() is calle
        checkTemplateFormat(newTemplate);

        List<Stat> newStats = new ArrayList<>();

        for(Stat stat : newTemplate.getTemplatestats()){
            newStats.add(statService.createStat(stat));
        }
        newTemplate.setTemplatestats(newStats);

        checkStatFormat(newTemplate.getTemplatestats());


        newTemplate = templateRepository.save(newTemplate);
        templateRepository.flush();

        log.debug("Created Information for Template: {}", newTemplate);
        return newTemplate;
    }

    public void checkTemplateFormat(Template templateToCheck){
        if(templateToCheck.getTemplatestats().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Template must have a name and at least on stat to be created.");
        }
    }


    public void checkStatFormat(List<Stat> statslist) {

        for (int i = 0; i < statslist.size(); i++) {
            Stat stat = statslist.get(i);
            if (stat.getStatvalue() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Template can't have a Stat Value");
            }
            String valuestype = stat.getValuestypes();
            if (stat.getStattype() == StatTypes.VALUE) {
                if (valuestype == null || valuestype.length()<1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Values Type must be a not empty String.");
                }
            }
            if (stat.getStattype() != StatTypes.VALUE) {
                if (valuestype != null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the Stattype VALUE can have a valuestype.");
                }
            }
            //Checking if Statname is Unique
            if (i + 1 < statslist.size()) {
                for (int j = i + 1; j < statslist.size(); j++) {
                    {
                        if (Objects.equals(stat.getStatname(), statslist.get(j).getStatname())) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No two stats of the same Template can have the same names");
                        }
                    }
                }
            }

        }
    }

    public Template getTemplateById(Long id){

        //checkIfIDExists(id);
        Optional<Template> potentialtemplate = templateRepository.findById(id);

        if(potentialtemplate.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided Template ID does not exist in the Database.");
        }
        return potentialtemplate.get();

    }



}

