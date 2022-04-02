package ch.uzh.ifi.hase.soprafs22.repository;


import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("templateRepository")
public interface TemplateRepository extends JpaRepository<Template, Long> {

}
