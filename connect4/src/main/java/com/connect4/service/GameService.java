package com.connect4.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.connect4.exceptions.GameEndedException;
import com.connect4.exceptions.GameNotExistException;
import com.connect4.exceptions.PlayerNotFoundException;
import com.connect4.exceptions.RoomFilledException;
import com.connect4.exceptions.WrongMoveException;
import com.connect4.exceptions.WrongTurnException;
import com.connect4.model.Game;
import com.connect4.model.Player;
import com.connect4.repo.GameRepository;
import com.connect4.util.Constants;
import com.connect4.util.Util;


/**
 * GameService class
 * @author mhamed
 *
 */

@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepo;
	
	
	/**
	 * This method creates a new game, which contains : 1)creates a new game and generate a gameId for it
	 * 													2)initiate the gameboard	
	 * 												    3)generate a player1 Id and pass a red 'r' piece color to it "you can simply change it to blue 'b'"
	 * 													4)save the created game to redias
	 * @return  the generated game
	 */
	public Game createGame(){
		Player player1 = new Player();
		Game game = new Game();
		
		String gameId = Util.generateId(Constants.GAME_PREFIX);  //generates gameId
		String player1Id = Util.generateId(Constants.PLAYER1_PREFIX);  //generates player1Id
		
		player1.setId(player1Id);
		player1.setPieceColor(Constants.RED_COLOR); // default color is red 'r', you can change it to 'b'
		game.setGameId(gameId);
		game.setPlayer1(player1);
		// initiate gameBoard
		game.initGameBoard(player1, gameId);  
		//save the game to redias
		gameRepo.saveGame(game);  
		return game;
	}
	
	
	/**
	 * This method joins a new player to an existing game
	 * @param gameId
	 * @return  the game details after join
	 * @throws Exception
	 */
	public Game joinGame(String gameId) throws Exception{
		Game game = gameRepo.getGame(gameId);
		// check if the game exist or not
		if(game == null){  
			throw new GameNotExistException("No game exist with this id to join!");
		}else{
			// check the status of the game, user can not join an ended game
			if(game.getGameStatus()){
				// case, if player 1 , have left the game and another player want to join
				if (game.getPlayer1() == null) {
					Player player1 = new Player();
					String player1Id = Util.generateId(Constants.PLAYER1_PREFIX);
					player1.setId(player1Id);
					player1.setPieceColor(game.getPlayer2().getPieceColor() == Constants.RED_COLOR ? Constants.BLUE_COLOR : Constants.RED_COLOR);
					game.setPlayer1(player1);
					// update redis with game details
					game = gameRepo.updateGame(game);
				} else if (game.getPlayer2() == null) { // case, if player 2, have left the game and another player want to join 
					Player player2 = new Player();
					String player2Id = Util.generateId(Constants.PLAYER2_PREFIX);
					player2.setId(player2Id);
					player2.setPieceColor(game.getPlayer1().getPieceColor() == Constants.RED_COLOR ? Constants.BLUE_COLOR: Constants.RED_COLOR);
					game.setPlayer2(player2);
					// update redis with game details
					game = gameRepo.updateGame(game);
				} else { // case , if the game have complete number of players and another player want to join
					throw new RoomFilledException("The room is already complete!");
				}

			}else{ // case, the game was ended and another player want to join
				throw new GameEndedException("Game ended!");
			}
		}
		return game;	
	}
	
	
	/**
	 * This method for playing the game
	 * @param gameId
	 * @param playerId
	 * @param colId
	 * @return
	 * @throws PlayerNotFoundException
	 * @throws WrongMoveException
	 * @throws WrongTurnException
	 * @throws GameEndedException
	 * @throws GameNotExistException
	 */
	public Game playGame(String gameId, String playerId, int colId) throws PlayerNotFoundException, WrongMoveException, WrongTurnException, GameEndedException, GameNotExistException {
		Game game = gameRepo.getGame(gameId);
		char pieceColor = '\0';
		// check if the game exist or not
		if(game == null){ 
			throw new GameNotExistException("No game exist with this id to join!");
		}else{
			// check the status of the game, user can not join an ended game
			if (game.getGameStatus()) { 
				//case, make sure that there are 2 players in the game before start playing
				if(game.getPlayer1() != null &&  game.getPlayer2() != null){ 
					if (playerId.equals(game.getPlayer1().getId())) {
						pieceColor = game.getPlayer1().getPieceColor();  //get piece color for the player
					} else if (playerId.equals(game.getPlayer2().getId())) {
						pieceColor = game.getPlayer2().getPieceColor(); //get piece color for the player
					} else { // case, this player is not registered in this game
						throw new PlayerNotFoundException("This player is not registered in this game session!");
					}				
				}else{ //case, make sure that there are 2 players in the game before start playing
					throw new PlayerNotFoundException("Please wait until all players joins the game!");
				}
				
				// if next move == '/0', then its the first move in the game, also check players turns
				if (game.getNextMove() == '\0' || game.getNextMove() == pieceColor) { 
					boolean isCorrectMove = game.insertPiece(colId, pieceColor); // check if player's move is correct or not
					if (isCorrectMove) {
						game.checkWinner(colId, pieceColor);
					} else { //throw if the move is wrong
						throw new WrongMoveException("Wrong move!");
					}
				} else { // throw if the turn is wrong
					throw new WrongTurnException("Wrong turn, it's the other player's turn!");
				}
				 // update game in redias
				gameRepo.updateGame(game); 
			} else { // thorw if game ended, and one of the players tries to play a move
				throw new GameEndedException("Game ended!");
			}			
		}
		return game;
	}
	
	
	/**
	 * This method for retrieving a game with it's id
	 * @param gameId
	 * @return
	 * @throws GameNotExistException
	 */
	public Game getGame(String gameId) throws GameNotExistException{
		Game game = gameRepo.getGame(gameId);
		// throw if the game does not exist
		if(game == null){ 
			throw new GameNotExistException("No game exist with this id!");
		}
		return game;
	}
	
	
	/**
	 * This method for retrieving all game sessions available
	 * @return
	 * @throws GameNotExistException
	 */
	public Map<Object, Object> getAllGameSessions() throws GameNotExistException{
		Map<Object, Object> allGameSessions = gameRepo.getAllGameSessions();
		 // throw if no game session exist
		if(allGameSessions.size() == 0){
			throw new GameNotExistException("No game sessions exist!");
		}
		return allGameSessions;
	}
	
	
	/**
	 * This method for deleting a game with it's id
	 * @param gameId
	 * @return
	 * @throws GameNotExistException
	 */
	public String deleteGame(String gameId) throws GameNotExistException{
		String deleteResult = "Game not deleted";
		Game game = gameRepo.getGame(gameId);
		// throw if the game id is not exist
		if(game == null){ 
			throw new GameNotExistException("No game exist with this id to delete!");
		}else{
			// delete from redias
			gameRepo.deleteGame(gameId);  
			deleteResult = "Game with id : " + gameId + " have been deleted!";
		}
		return deleteResult;
	}
	
	
	/**
	 * This method for leaving a game
	 * @param gameId
	 * @param playerId
	 * @return
	 * @throws PlayerNotFoundException
	 * @throws GameNotExistException
	 */
	public Game leaveGame(String gameId, String playerId) throws PlayerNotFoundException, GameNotExistException{
		Game game = gameRepo.getGame(gameId);
		// check if the game exist or not
		if(game == null){ 
			throw new GameNotExistException("No game exist with this id to join!");
		}else{ // if game exist
			
			// check is there players registered in the game or not
			if(game.getPlayer1() == null && game.getPlayer2() == null){ 
				throw new PlayerNotFoundException("No players registered in this game session!");
			}else{ 
				if(game.getPlayer1() != null){
					// if playerId equals player1 , then remove him from the game and update the game in redais
					if(game.getPlayer1().getId().equals(playerId)){  
						game.setPlayer1(null);		
						// update redias
						game = gameRepo.updateGame(game); 
					}
				}
				
				if(game.getPlayer2() != null){
					 // if playerId equals player2 , then remove him from the game and update the game in redais
					if(game.getPlayer2().getId().equals(playerId)){
						game.setPlayer2(null);			
						// update redias
						game = gameRepo.updateGame(game); 
					}
				}
				
				//check if the playerId is in the game
				if(game.getPlayer1() != null && !game.getPlayer1().getId().equals(playerId) && game.getPlayer2() != null && !game.getPlayer2().getId().equals(playerId)){
					throw new PlayerNotFoundException("This player is not registered in this game session!");
				}				
			}
		}
		return game;	
	}
}
