package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SessionControllerTest
 * This is a WebMvcTest which allows to test the SessionController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the SessionController works.
 */
@WebMvcTest(GameController.class)
public class GameControllerTest {
    Game game;

    @BeforeEach
    public void init() {
        game = new Game();
        game.setGameCode(1L);
        //game.setGameId(1L);
        game.setCurrentStatName(null);
        game.setRoundStatus(null);
        game.setPlayerList(new ArrayList<>());
        game.setWinner(null);
        game.setCurrentPlayer(1L);
        game.setOpponentPlayer(null);

    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    EntityManager createentityManager;





    @Test //post /game -> 201 : successful creation of game
    public void createGameSuccess() throws Exception {
        // given predefined game

        given(gameService.createGame(Mockito.any())).willReturn(game);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/"+game.getGameCode()+"/game")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", "auth");
        // then
        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.currentPlayer", is(game.getCurrentPlayer().intValue())))
                .andExpect(jsonPath("$.opponentPlayer", is(game.getOpponentPlayer())))
                .andExpect(jsonPath("$.playerList", is(game.getPlayerList())))
                .andExpect(jsonPath("$.winner", is(game.getWinner())))
                .andReturn();
    }

    @Test //get /get/1 -> 200 : successfully retrieve data for game with ID 1
    public void getGameByGameCode() throws Exception {
        // given predfined game
        // this mocks the gameService -> we define above what the gameService should return when getGameByGameCode() is called
        given(gameService.findGameByGameCode(game.getGameCode())).willReturn(game);

        // when
        MockHttpServletRequestBuilder getRequest = get("/session/"+game.getGameCode()+"/game").contentType(MediaType.APPLICATION_JSON).header("Authentication", "auth");

        // then
        mockMvc.perform(getRequest).andExpect(status().is(200))
                .andExpect(jsonPath("$.currentPlayer", is(game.getCurrentPlayer().intValue())))
                .andExpect(jsonPath("$.opponentPlayer", is(game.getOpponentPlayer())))
                .andExpect(jsonPath("$.playerList", is(game.getPlayerList())))
                .andExpect(jsonPath("$.winner", is(game.getWinner())))
                .andReturn();
    }

    @Test //post /session/gamecode/game -> 404: corresponding session not found
    public void createGameWithWrongGameCode() throws Exception{
        // given predefined session
        given(gameService.createGame(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.resolve(404), "There exists no Session with given gamecode"));
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/session/"+0L+"/game")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", "auth");
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().is(404));
    }


    /*
    @Test //Put /game/{gameCode}/round -> 200 : successfully updated opponent gets returned
    public void updateOpponentPlayerSuccess() throws Exception {
        // given predefined session
        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setOpponentPlayer(2L);
        gamePutDTO.setCurrentStatName(null);

        given(gameService.gameUpdate(Mockito.anyLong(),Mockito.anyLong(), Mockito.anyString())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/session/"+game.getGameCode()+"/round")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePutDTO));

        // then
        MvcResult mvcResult = mockMvc.perform(putRequest)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.currentStatName", is(game.getCurrentStatName())))
                .andExpect(jsonPath("$.roundStatus", is(game.getRoundStatus())))
                .andReturn();
    }
    */

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }
}