package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("statsRepository")
public interface StatRepository extends JpaRepository<Stat, Long> {

    Stat findByStatId(Long StatId);


}
