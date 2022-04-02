package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Stats;
import ch.uzh.ifi.hase.soprafs22.entity.Stats;
import ch.uzh.ifi.hase.soprafs22.repository.StatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StatsService {
    private final Logger log = LoggerFactory.getLogger(StatsService.class);

    private final StatsRepository statsRepository;

    @Autowired
    public StatsService(@Qualifier("statsRepository") StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public List<Stats> getStatss() {
        return this.statsRepository.findAll();
    }



    public Stats createStats(Stats newStats) {

        // saves the given entity but data is only persisted in the database once
        // flush() is called


        newStats = statsRepository.save(newStats);
        statsRepository.flush();

        log.debug("Created Information for Stats: {}", newStats);
        return newStats;
    }


    public Stats getStatsById(Long id){

        //checkIfIDExists(id);
        Optional<Stats> potentialstats = statsRepository.findById(id);

        //change Null to Error
        return potentialstats.orElse(null);



    }



}

