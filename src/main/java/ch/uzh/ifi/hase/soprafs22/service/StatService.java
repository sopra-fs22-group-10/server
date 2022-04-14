package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import ch.uzh.ifi.hase.soprafs22.repository.StatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

@Service
@Transactional
public class StatService {
    private final Logger log = LoggerFactory.getLogger(StatService.class);

    private final StatRepository statRepository;

    @Autowired
    public StatService(@Qualifier("statsRepository") StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    /* I don't think the get Stats should exist because they are already saved in the Template or crad Entity
    public List<Stats> getStats() {
        return this.statsRepository.findAll();
    }

     */


    public Stat createStat(Stat newStat) {

        if(newStat.getStatname() == null || newStat.getStattype() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A created Stat has to have at least a statnam and stattype");
        }

        newStat = statRepository.save(newStat);
        statRepository.flush();

        log.debug("Created Information for Stats: {}", newStat);
        return newStat;
    }


    public Stat getStatById(Long id){

        //checkIfIDExists(id);
        Optional<Stat> potentialStat = statRepository.findById(id);

        //change Null to Error
        if(potentialStat.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided Stat ID does not exist in the Database.");
        }
        return potentialStat.get();

    }



}

