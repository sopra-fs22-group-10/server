package ch.uzh.ifi.hase.soprafs22.rest.mapper;


import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Session;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.Template;


import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


  @Mapping(source = "templatestats", target = "templatestats")
  @Mapping(source = "statcount", target = "statcount")
  @Mapping(target = "templateId", ignore = true)
  Template convertTemplatePostDTOtoEntity(TemplatePostDTO templatePostDTO);

  @Mapping(source = "deckname", target = "deckname")
  @Mapping(target = "cardList", ignore = true)
  @Mapping(target = "deckId", ignore = true)
  @Mapping(target ="deckstatus", ignore = true)
  @Mapping(target = "template", ignore = true)
  Deck convertDeckPostDTOtoEntity(DeckPostDTO deckPostDTO);

  @Mapping(source = "cardstats", target = "cardstats")
  @Mapping(source = "cardname", target = "cardname")
  @Mapping(source = "image", target = "image")
  @Mapping(source = "cardId", target = "cardId")
  Card convertCardPutDTOtoEntity(CardPutDTO cardPutDTO);


  @Mapping(source = "cardstats", target = "cardstats")
  @Mapping(source = "cardname", target = "cardname")
  @Mapping(source = "image", target = "image")
  @Mapping(target = "cardId", ignore = true)
  Card convertCardPostDTOtoEntity(CardPostDTO cardPostDTO);

    /*
    @Mapping(source = "username", target = "username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authentication", ignore = true)
    @Mapping(target = "status", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);
    *
     */

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authentication", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deckList", ignore = true)
    User convertUserLoginDTOtoEntity(UserLoginDTO userLoginDTO);



    //sessionMappings
    @Mapping(source = "hostUsername", target = "hostUsername")
    @Mapping(source = "deckId", target = "deckId")
    @Mapping(source = "maxPlayers", target = "maxPlayers")
    Session convertSessionPostDTOtoEntity(SessionPostDTO sessionPostDTO);

    @Mapping(source = "gameCode", target = "gameCode")
    @Mapping(source = "hostUsername", target = "hostUsername")
    @Mapping(source = "userList", target = "userList")
    @Mapping(source = "maxPlayers", target = "maxPlayers")
    @Mapping(source = "deckId", target = "deckId")
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
