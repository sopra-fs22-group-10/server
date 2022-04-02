package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.entity.Stats;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TemplateService {
    private final Logger log = LoggerFactory.getLogger(TemplateService.class);

    private final TemplateRepository templateRepository;

    private final StatsService statsService;

    @Autowired
    public TemplateService(@Qualifier("templateRepository") TemplateRepository templateRepository, StatsService statsService) {
        this.templateRepository = templateRepository;

        this.statsService = statsService;
    }
    /*
    *Not sure if get Templates is necessary
    public List<Template> getTemplates() {
        return this.templateRepository.findAll();
    }

     */

    public void changeTemplate(Template newTemplate){

        Template templateToChange = getTemplateById(newTemplate.getTemplateid());
        templateToChange = newTemplate;

        templateRepository.flush();
        log.debug("Updated Information for Template: {}", templateToChange);
    }

    public Template createTemplate(Template newTemplate) {

        // saves the given entity but data is only persisted in the database once
        // flush() is calle
        List<Stats> newStats = new ArrayList<>();
        for(Stats stat : newTemplate.getTemplatestats()){
            newStats.add(statsService.createStats(stat));
        }
        newTemplate.setTemplatestats(newStats);

        newTemplate = templateRepository.save(newTemplate);
        templateRepository.flush();

        log.debug("Created Information for Template: {}", newTemplate);
        return newTemplate;
    }


    public Template getTemplateById(Long id){

        //checkIfIDExists(id);
        Optional<Template> potentialtemplate = templateRepository.findById(id);

        //change Null to Error
        return potentialtemplate.orElse(null);



    }



}

