package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Deck;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeckService deckService;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setAuthentication("testAuthentication");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setUserId(1L);


        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getStatus(), createdUser.getStatus());
        //not in get
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getAuthentication(), createdUser.getAuthentication());
    }

    @Test
    public void addDeck_to_User(){
        Deck deck = new Deck();
        deck.setDeckId(33L);
        testUser.setDeckList(new ArrayList<>());
        Mockito.when(deckService.getDeckById(Mockito.any())).thenReturn(deck);
        Optional<User> lUser = Optional.of(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(lUser);
        userService.addDeck(deck.getDeckId(), testUser.getUserId());
        assertEquals(testUser.getDeckList().get(0), deck);
    }
    @Test
    public void getUser_by_Authentication(){

        Mockito.when(userRepository.findByAuthentication(Mockito.any())).thenReturn(testUser);
        User createdUser = userService.getUserByAuthentication(testUser.getAuthentication());

        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getStatus(), createdUser.getStatus());
        //not in get
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getAuthentication(), createdUser.getAuthentication());
    }

    @Test
    public void removeDeck_from_User(){
        Deck deck = new Deck();
        deck.setDeckId(2L);
        List<Deck> deckList = new ArrayList<>();
        deckList.add(deck);
        testUser.setDeckList(deckList);

        Mockito.when(deckService.getDeckById(Mockito.any())).thenReturn(deck);
        Optional<User> lUser = Optional.of(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(lUser);


        userService.removeDeck(deck.getDeckId(), testUser.getUserId());
        assertEquals(testUser.getDeckList(), Collections.emptyList());
    }
    @Test
    public void accessUser_success(){
        testUser.setAuthentication("Authentication");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        User createdUser = userService.accessUser(testUser);

        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getStatus(), createdUser.getStatus());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getAuthentication(), createdUser.getAuthentication());

    }

    @Test
    public void logoutUser_success(){

        String previous_authentication = testUser.getAuthentication();
        Optional<User>lUser = Optional.of(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(lUser);
        Mockito.when(userRepository.findByAuthentication(Mockito.any())).thenReturn(testUser);
        userService.logoutUser(lUser.get().getUserId(), testUser.getAuthentication());
        System.out.println(testUser.getAuthentication());
        System.out.println(lUser.get().getAuthentication());
        assertNotEquals(previous_authentication, testUser.getAuthentication());

    }

    @Test
    public void checkLoginCredentials_success(){

    }

    @Test
    public void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }
    @Test
    public void deleteUser_success(){

    }

    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }


}
