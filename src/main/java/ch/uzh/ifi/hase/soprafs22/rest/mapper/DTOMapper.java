package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;

import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


  @Mapping(source = "templatestats", target = "templatestats")
  @Mapping(source = "templatename", target = "templatename")
  @Mapping(source = "statcount", target = "statcount")
  @Mapping(target = "templateId", ignore = true)
  Template convertTemplatePostDTOtoEntity(TemplatePostDTO templatePostDTO);

  @Mapping(source = "deckname", target = "deckname")
  @Mapping(target = "cardlist", ignore = true)
  @Mapping(target = "deckId", ignore = true)
  @Mapping(target ="deckstatus", ignore = true)
  @Mapping(target = "template", ignore = true)
  Deck convertDeckPostDTOtoEntity(DeckPostDTO deckPostDTO);



    @Mapping(source = "username", target = "username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authentication", ignore = true)
    @Mapping(target = "status", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authentication", ignore = true)
    @Mapping(target = "status", ignore = true)
    User convertUserLoginDTOtoEntity(UserLoginDTO userLoginDTO);



    //sessionMappings
    @Mapping(source = "username", target = "username")
    @Mapping(source = "deckId", target = "deckId")
    @Mapping(source = "maxPlayers", target = "maxPlayers")
    Session convertSessionPostDTOtoEntity(SessionPostDTO sessionPostDTO);

    @Mapping(source = "sessionId", target = "sessionId")
    @Mapping(source = "gameCode", target = "gameCode")
    SessionGetDTO convertEntityToSessionGetDTO(Session session);

}
/*
@Mapping(source = "deckId", target = "deckId")
    @Mapping(source = "cardlist", target = "cardlist")
    @Mapping(source = "deckname", target = "deckname")
    @Mapping(source ="deckstatus", target= "deckstatus")
    @Mapping(source ="template", target ="template")
    DeckGetDTO convertEntityToDeckGetDTO(Deck deck);
 */
