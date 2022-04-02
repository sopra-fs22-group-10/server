package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Stats;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("statsRepository")
public interface StatsRepository extends JpaRepository<Stats, Long> {

}
