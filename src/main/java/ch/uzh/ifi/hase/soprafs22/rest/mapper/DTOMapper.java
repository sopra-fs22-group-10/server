package ch.uzh.ifi.hase.soprafs22.rest.mapper;


import ch.uzh.ifi.hase.soprafs22.entity.*;


import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


  @Mapping(source = "templatestats", target = "templatestats")
  @Mapping(target = "templateId", ignore = true)
  Template convertTemplatePostDTOtoEntity(TemplatePostDTO templatePostDTO);

  @Mapping(source = "deckname", target = "deckname")
  @Mapping(target = "cardList", ignore = true)
  @Mapping(target = "deckId", ignore = true)
  @Mapping(source = "deckstatus", target ="deckstatus")
  @Mapping(target = "template", ignore = true)
  @Mapping(target ="deckaccesscode", ignore = true)
  @Mapping(source = "deckImage", target = "deckImage")
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

  @Mapping(source = "deckname", target = "deckname")
  @Mapping(target = "deckaccesscode", ignore = true)
  @Mapping(source = "deckImage",target = "deckImage")
  @Mapping(target = "cardList", ignore = true)
  @Mapping(target = "deckId", ignore = true)
  @Mapping(target ="deckstatus", ignore = true)
  @Mapping(target = "template", ignore = true)
  Deck convertDeckPutDTOtoEntity(DeckPutDTO deckPutDTO);

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
    @Mapping(source = "hostId", target = "hostId")
    @Mapping(source = "deckId", target = "deckId")
    @Mapping(source = "maxPlayers", target = "maxPlayers")
    @Mapping(target = "sessionId", ignore = true)
    @Mapping(target = "gameCode", ignore = true)
    @Mapping(target = "userList", ignore = true)
    @Mapping(target = "hasGame", ignore = true)
    Session convertSessionPostDTOtoEntity(SessionPostDTO sessionPostDTO);

    @Mapping(source = "gameCode", target = "gameCode")
    @Mapping(source = "hostUsername", target = "hostUsername")
    @Mapping(source = "hostId", target = "hostId")
    @Mapping(source = "userList", target = "userList")
    @Mapping(source = "maxPlayers", target = "maxPlayers")
    @Mapping(source = "deckId", target = "deckId")
    @Mapping(source = "hasGame",target = "hasGame")
    @Mapping(source = "deckaccesscode",target = "deckaccesscode")
    SessionGetDTO convertEntityToSessionGetDTO(Session session);


    @Mapping(source = "currentPlayer", target = "currentPlayer")
    @Mapping(source = "opponentPlayer", target = "opponentPlayer")
    @Mapping(source = "playerList", target = "playerList")
    @Mapping(source = "winner", target = "winner")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "currentStatName", target = "currentStatName")
    @Mapping(source = "roundStatus", target = "roundStatus")
    RoundGetDTO convertEntityToRoundGetDTO(Game game);

}
/*
@Mapping(source = "deckId", target = "deckId")
    @Mapping(source = "cardlist", target = "cardlist")
    @Mapping(source = "deckname", target = "deckname")
    @Mapping(source ="deckstatus", target= "deckstatus")
    @Mapping(source ="template", target ="template")
    DeckGetDTO convertEntityToDeckGetDTO(Deck deck);
 */
