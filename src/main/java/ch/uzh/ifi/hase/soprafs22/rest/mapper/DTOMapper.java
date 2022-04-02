package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "password", target = "password")
  @Mapping(source = "username", target = "username")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "birthdate", target = "birthdate")
  @Mapping(source = "username", target = "username")
  @Mapping(source ="token", target ="token")
  @Mapping(source = "status", target = "status")
  UserGetDTO convertEntityToUserGetDTO(User user);


  @Mapping(source = "deckId", target = "deckId")
  @Mapping(source = "cardlist", target = "cardlist")
  @Mapping(source = "deckname", target = "deckname")
  @Mapping(source ="template", target ="template")
  DeckGetDTO convertEntityToDeckGetDTO(Deck deck);

  @Mapping(source = "deckname", target = "deckname")
  Deck convertDeckPostDTOtoEntity(DeckPostDTO deckPostDTO);



  //@Mapping(source = "templateid", target = "templateid")
  @Mapping(source = "templatestats", target = "templatestats")
  @Mapping(source = "templatename", target = "templatename")
  @Mapping(source = "statcount", target = "statcount")
  Template convertTemplatePostDTOtoEntity(TemplatePostDTO templatePostDTO);


}
