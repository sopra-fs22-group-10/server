package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sessionRepository")
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findBySessionId(Long sessionId);

    Session findByGameCode(int gameCode);

    void deleteByGameCode(int gameCode);
}
