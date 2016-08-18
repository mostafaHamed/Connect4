package com.connect4.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.connect4.model.Game;
import com.connect4.service.GameService;

/**
 * Controller class contains all rest end-points for the application
 * @author mhamed
 *
 */

@RestController
@RequestMapping(value="/connect4/api/")
public class GameController {
	
	@Autowired
	GameService gameService;
	
	
	/**
	 * Rest end-point to create a new game
	 * @return the game that have been created
	 */
	@RequestMapping(value="game/create" , method = RequestMethod.POST)
	public ResponseEntity<?> createNewGame(){
		Game game = gameService.createGame();
		return new ResponseEntity<>(game, HttpStatus.CREATED);
	}
	
	
	/**
	 * Rest end-point to join a game, takes the id of the game that user want to join
	 * @param gameId  
	 * @return  the game with all details after joining
	 * @throws Exception   returns exception in the following cases: 1)gameId does not exist     2)room completed 'room have 2 players' while another user tries to join
	 */
	@RequestMapping(value="game/{gameId}/join" , method = RequestMethod.POST)
	public ResponseEntity<?> joinGame(@PathVariable("gameId") String gameId) throws Exception{
		Game game = gameService.joinGame(gameId);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}
	
	
	/**
	 * Rest end-point to play the game, takes the gameId that user want to play , playerId and the column number to place a piece
	 * @param gameId
	 * @param playerId
	 * @param colId
	 * @return game details after last move
	 * @throws Exception  returns exception in the following cases: 1)gameId does not exist       2)playerId is not registered in this game session to play    3)wrong move from one of the players
	 *                                                              4)wrong player turn   5)no sufficient players to start play  6)game ended and one of players tries to play a piece
	 */
	@RequestMapping(value="game/{gameId}/player/{playerId}/col/{colId}/play" , method = RequestMethod.POST)
	public ResponseEntity<?> playGame(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId, @PathVariable("colId") int colId) throws Exception{
		Game game = gameService.playGame(gameId, playerId, colId);
		String winner = game.getWinnerIfExist();
		return new ResponseEntity<>(game, HttpStatus.OK);
	}
	
	
	/**
	 * Rest end-point to get a game with it's id
	 * @param gameId
	 * @return game details
	 * @throws Exception  returns exception in the following cases: 1)gameId does not exist
	 */
	@RequestMapping(value="game/{gameId}/get" , method = RequestMethod.GET)
	public ResponseEntity<?> getGame(@PathVariable("gameId") String gameId) throws Exception{
		Game game = gameService.getGame(gameId);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}
	
	
	/**
	 * Rest end-point to leave a game, takes gameId and playerId who wants to leave
	 * @param gameId
	 * @param playerId
	 * @return game details after leaving of one of the players
	 * @throws Exception  returns exception in the following cases: 1)gameId does not exist      2)if this game have no players 'both player1 and player2 have left the game'
	 * 																3)playerId is not registered in this game session to leave
	 */
	@RequestMapping(value="game/{gameId}/player/{playerId}/leave" , method = RequestMethod.POST)
	public ResponseEntity<?> leaveGame(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) throws Exception{
		Game game = gameService.leaveGame(gameId, playerId);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}
	
	
	
	/**
	 * Rest end-point to retrieve all game sessions
	 * @return  a map contains all game session with it's details
	 * @throws Exception   returns exception in the following cases: 1)no game sessions founded
	 */
	@RequestMapping(value="game/getAllGameSessions" , method = RequestMethod.GET)
	public ResponseEntity<?> getAllGameSessions() throws Exception{
		Map<Object, Object> allGameSessions = gameService.getAllGameSessions();
		return new ResponseEntity<>(allGameSessions, HttpStatus.OK);
	}
	
	
	/**
	 * Rest end-point to delete a game with it's gameId
	 * @param gameId
	 * @return  message that indicates the game have been deleted or not 
	 * @throws Exception    returns exception in the following cases: 1)no game with this id was founded
	 */
	@RequestMapping(value="game/{gameId}/delete" , method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGame(@PathVariable("gameId") String gameId) throws Exception{
		String deleteResult = gameService.deleteGame(gameId);
		return new ResponseEntity<>(deleteResult, HttpStatus.OK);
	}

}
