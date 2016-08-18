package com.connect4;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.connect4.model.Game;
import com.connect4.model.Player;
import com.connect4.repo.GameRepository;


/**
 * Controller test
 * @author mhamed
 *
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Connect4Application.class)
@WebAppConfiguration
public class Connect4ApplicationTests {

	
	@Test
	public void contextLoads() {
	}

	@Autowired
	 private WebApplicationContext context;
	 private MockMvc mockMvc;
	 
	 @Autowired
	 private GameRepository gameRepo;

	 @Before
	 public void setUp() {
	  mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	 }
	 
	 
	 @Test
	 public void createGamePositiveTest() throws Exception {
		 mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated());		 
	 }
	 
	 @Test
	 public void joinGamePositiveTest() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 mockMvc.perform(post("/connect4/api/game/" + gameObjectAsJson.getString("gameId")+"/join/")).andExpect(status().isOk());		 
	 }
	 
	 @Test
	 public void joinGameNegativeTest_GameNotExist() throws Exception {
		 String fakeGameId = "C4_ghjfjdksljsdjakh";
		 mockMvc.perform(post("/connect4/api/game/" + fakeGameId + "/join/")).andExpect(status().isNotFound());		 
	 }
	 
	 @Test
	 public void joinGameNegativeTest_GameEnded() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 game.setGameStatus(false);
		 gameRepo.updateGame(game);
		 mockMvc.perform(post("/connect4/api/game/" + game.getGameId() +"/join/")).andExpect(status().isLocked());		 
	 }
	 
	 
	 @Test
	 public void joinGameNegativeTest_RoomCompleted() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 gameRepo.updateGame(game);
		 mockMvc.perform(post("/connect4/api/game/" + game.getGameId() +"/join/")).andExpect(status().isLocked());		 
	 }

	 @Test
	 public void playGamePositiveTest() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 String colId = "2";
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 game.setNextMove('r');
		 gameRepo.updateGame(game);
		 mockMvc.perform(post("/connect4/api/game/" +  game.getGameId()  + "/player/" + game.getPlayer1().getId() + "/col/" + colId + "/play" )).andExpect(status().isOk());		 
	 }
	 
	 @Test
	 public void playGameNegativeTest_GameNotExist() throws Exception {
		 String fakeGameId = "C4_ghjfjdksljsdjakh";
		 String fakePlayerId = "P1_ghgdm";
		 String colId = "2";
		 mockMvc.perform(post("/connect4/api/game/" + fakeGameId + "/player/" + fakePlayerId + "/col/" + colId + "/play" )).andExpect(status().isNotFound());		 				 
	 }
	 
	 
	 @Test
	 public void playGameNegativeTest_GameEnded() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 game.setGameStatus(false);
		 gameRepo.updateGame(game);
		 String fakePlayerId = "P1_ghgdm";
		 String colId = "2";
		 mockMvc.perform(post("/connect4/api/game/" + game.getGameId()  + "/player/" + fakePlayerId + "/col/" + colId + "/play" )).andExpect(status().isLocked());		 
	 }
	 
	 
	 @Test
	 public void playGameNegativeTest_NoCompleteNumberOfPlayers() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 String colId = "2";
		 String player1Id = game.getPlayer1().getId();
		 mockMvc.perform(post("/connect4/api/game/" +  game.getGameId()  + "/player/" + player1Id + "/col/" + colId + "/play" )).andExpect(status().isNotFound());		 
	 }
	 
	 @Test
	 public void playGameNegativeTest_PlayerIsNotRegistered() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 String colId = "2";
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 gameRepo.updateGame(game);
		 mockMvc.perform(post("/connect4/api/game/" +  game.getGameId()  + "/player/" + "fakePlayerId67i68789" + "/col/" + colId + "/play" )).andExpect(status().isNotFound());		 
	 }
	 
	 @Test
	 public void playGameNegativeTest_WrongMove() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 String colId = "9";
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 gameRepo.updateGame(game);
		 mockMvc.perform(post("/connect4/api/game/" +  game.getGameId()  + "/player/" + game.getPlayer1().getId() + "/col/" + colId + "/play" )).andExpect(status().isBadRequest());		 
	 }
	 
	 @Test
	 public void playGameNegativeTest_WrongTurn() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 String colId = "9";
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 game.setNextMove('b');
		 gameRepo.updateGame(game);
		 mockMvc.perform(post("/connect4/api/game/" +  game.getGameId()  + "/player/" + game.getPlayer1().getId() + "/col/" + colId + "/play" )).andExpect(status().isBadRequest());		 
	 }
	 
	 
	 @Test
	 public void getGamePositiveTest() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 String gameId = gameObjectAsJson.getString("gameId");
		 mockMvc.perform(get("/connect4/api/game/" +  gameId  + "/get/")).andExpect(status().isOk());		 
	 }
	 
	 @Test
	 public void getGameNegativeTest_GameNotExist() throws Exception {
		 String fakeGameId = "C4_ghjfjdksljsdjakh";		
		 mockMvc.perform(get("/connect4/api/game/" +  fakeGameId  + "/get/")).andExpect(status().isNotFound());		 
	 }
	 
	 @Test
	 public void getAllGameSessionsPositiveTest() throws Exception {
		 mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 mockMvc.perform(get("/connect4/api/game/getAllGameSessions")).andExpect(status().isOk());		 
	 }
	 
	
	 @Test
	 public void deleteGamePositiveTest() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 String gameId = gameObjectAsJson.getString("gameId");
		 mockMvc.perform(delete("/connect4/api/game/" +  gameId  + "/delete/")).andExpect(status().isOk());		 
	 }
	 
	 
	 @Test
	 public void deleteGameNegativeTest_GameNotExist() throws Exception {
		 String fakeGameId = "C4_ghjfjdksljsdjakh";
		 mockMvc.perform(delete("/connect4/api/game/" +  fakeGameId  + "/delete/")).andExpect(status().isNotFound());		 
	 }
	 
	 @Test
	 public void leaveGamePositiveTest() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 gameRepo.updateGame(game);
		 mockMvc.perform(post("/connect4/api/game/" +  game.getGameId()  + "/player/" + player2.getId() + "/leave/")).andExpect(status().isOk());		 
	 }
	 
	 
	 @Test
	 public void leaveGameNegativeTest_GameNotExist() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 gameRepo.updateGame(game);
		 String fakeGameId = "C4_hgfjdskh";
		 mockMvc.perform(post("/connect4/api/game/" +  fakeGameId  + "/player/" + player2.getId() + "/leave/")).andExpect(status().isNotFound());		 
	 }
	 
	 
	 @Test
	 public void leaveGameNegativeTest_NoPlayerRegistered() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 game.setPlayer1(null);
		 gameRepo.updateGame(game);
		 String fakePlayerId = "P1_hgfjdskh";
		 mockMvc.perform(post("/connect4/api/game/" +  game.getGameId()  + "/player/" + fakePlayerId + "/leave/")).andExpect(status().isNotFound());		 
	 }
	 
	 @Test
	 public void leaveGameNegativeTest_PlayerIsNotRegistered() throws Exception {
		 MvcResult result = mockMvc.perform(post("/connect4/api/game/create/")).andExpect(status().isCreated()).andReturn();
		 String responseContent = result.getResponse().getContentAsString();
		 JSONObject gameObjectAsJson = new JSONObject(responseContent);
		 Game game = gameRepo.getGame(gameObjectAsJson.getString("gameId"));
		 Player player2 = new Player();
		 player2.setId("P2_hjklkjh");
		 player2.setPieceColor('b');
		 game.setPlayer2(player2);
		 gameRepo.updateGame(game);
		 String fakePlayerId = "C4_hgfjdskh";
		 mockMvc.perform(post("/connect4/api/game/" + game.getGameId() + "/player/" + fakePlayerId + "/leave/")).andExpect(status().isNotFound());		 
	 }
}
