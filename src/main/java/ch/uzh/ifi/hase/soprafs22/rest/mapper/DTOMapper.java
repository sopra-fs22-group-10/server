package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Card;
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
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "birthdate", ignore = true)
  @Mapping(target = "token", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "birthdate", target = "birthdate")
  @Mapping(source = "username", target = "username")
  @Mapping(source ="token", target ="token")
  @Mapping(source = "status", target = "status")
  UserGetDTO convertEntityToUserGetDTO(User user);

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

  
  @Mapping(source = "cardstats", target = "cardstats")
  @Mapping(source = "cardname", target = "cardname")
  @Mapping(source = "image", target = "image")
  @Mapping(target = "cardId", ignore = true)
  Card convertCardPostDTOtoEntity(CardPostDTO cardPostDTO);

}
/*
@Mapping(source = "deckId", target = "deckId")
    @Mapping(source = "cardlist", target = "cardlist")
    @Mapping(source = "deckname", target = "deckname")
    @Mapping(source ="deckstatus", target= "deckstatus")
    @Mapping(source ="template", target ="template")
    DeckGetDTO convertEntityToDeckGetDTO(Deck deck);
 */
