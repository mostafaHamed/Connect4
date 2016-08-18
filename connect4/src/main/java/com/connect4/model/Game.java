package com.connect4.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.connect4.util.Constants;

/**
 * Game model class
 * @author mhamed
 *
 */
public class Game implements Serializable{
	
	private static final long serialVersionUID = -7983851303404120872L;
	
	String gameId;  //generated Is
	char [][]board = new char[8][8];  //board size	
	Player player1; //default color is red
	Player player2; //default color is blue

	boolean isRunningGame = true; // if true then the game is still open, no winners, else false
	boolean winners [] = new boolean [2]; // [0] for 'r', [1] for 'b'
	char nextMove;

	int initialArraySize = 0;
	ArrayList<Integer> rowSizeList = new ArrayList<Integer>(8);


	/**
	 * This method initiates the gameboard and the game session
	 * @param player1
	 * @param gameId
	 */
	public void initGameBoard(Player player1, String gameId){
		this.player1 = player1;
		this.gameId = gameId;
		initBoard();
	}

	
	/**
	 * An initializer method that initializes the board with empty pieces
	 */
	void initBoard(){
		for(int i = 0; i <= 7; i++){
			for(int j = 0; j <= 7; j++){
				board[i][j] = '-';
			}
		}
		
		for(int i = 0; i <= 7; i++){
			rowSizeList.add(i, 7);
		}
	}
	
	/**
	 * This method insert tries to insert a piece in the board, if the peace have been inserted successfully , the method will return true, else false
	 * @param colNumber
	 * @param pieceColor
	 * @return true if the insert was successful, else false
	 */
	public boolean insertPiece(int colNumber, char pieceColor){
		boolean isSuccessfulInsert = false;
		
		if(colNumber > Constants.MAX_COL_NUMBER || colNumber < Constants.MIN_COL_NUMBER){  // check column boundaries
			return isSuccessfulInsert;
		}
		
		int row = rowSizeList.get(colNumber);
		if(row < Constants.MIN_ROW_NUMBER){ // check row boundaries
			return isSuccessfulInsert;
		}
		
		if(initialArraySize < 6){  // no need for checks if the board have number of pieces < 6
			board[rowSizeList.get(colNumber)][colNumber] = pieceColor == Constants.RED_COLOR ? Constants.RED_COLOR : Constants.BLUE_COLOR;
			rowSizeList.set(colNumber, rowSizeList.get(colNumber) - 1);
			initialArraySize++;
			isSuccessfulInsert = true;
		}else{
			board[rowSizeList.get(colNumber)][colNumber] = pieceColor == Constants.RED_COLOR ? Constants.RED_COLOR : Constants.BLUE_COLOR;
			rowSizeList.set(colNumber, rowSizeList.get(colNumber) - 1);
			isSuccessfulInsert = true;
		}
		return isSuccessfulInsert;
	}
	
	/**
	 * A helper method checks on the count of repeated color and sets a winner if exist , and sets the game status
	 * @param countOfRepeatedColor
	 * @param pieceColor
	 */
	void setWinnerIfExist(int countOfRepeatedColor, char pieceColor){
		if (countOfRepeatedColor == Constants.WINNING_NUMBER) {
			if(pieceColor == Constants.RED_COLOR){
				winners[0] = true;  // set winner is player who holds the red piece
				setGameStatus(false); // set game status 'ended'
				setNextMove('\0'); //no need to specify the next move, game ended
			}else{
				winners[1] = true; // set winner is player who holds the blue piece
				setGameStatus(false); //set game status 'ended'
				setNextMove('\0'); //no need to specify the next move, game ended
			}
		}
		setNextMove(pieceColor == Constants.RED_COLOR ? Constants.BLUE_COLOR : Constants.RED_COLOR);
	}
	
	
	/**
	 * This method returns the winner if exist
	 * @return
	 */
	public String getWinnerIfExist() {
		String winner = "";
		if (winners[0] == true) { 
			// [0] holds red piece, check which one of the players who holding red piece to set his as a winner
			String playerId = (getPlayer1().getPieceColor() == Constants.RED_COLOR) ? getPlayer1().getId() : getPlayer2().getId();
			winner =  "Player:" + playerId +" is the winner";
		}
		if (winners[1] == true) {
			// [1] holds blue piece, check which one of the players who holding blue piece to set his as a winner
			String playerId = (getPlayer1().getPieceColor() == Constants.BLUE_COLOR) ? getPlayer1().getId() : getPlayer2().getId();
			winner = "Player:" + playerId +" is the winner";
		}
		return winner;
	}
	
	/**
	 * A main method that checks for the winner after the insertion of the piece and sets the winner if exist
	 * @param colNumber
	 * @param pieceColor
	 * @return
	 */
	public void checkWinner(int colNumber, char pieceColor){
		int row = rowSizeList.get(colNumber) + 1;
		int col = colNumber;
		int countOfRepeatedColor = 0;
		
		// down check
		for(int i = row; i <= Constants.MAX_ROW_NUMBER; i++){
			if(board[i][col] == pieceColor){
				countOfRepeatedColor++;
			}else{
				countOfRepeatedColor = 0;
				break;
			}
		}
		setWinnerIfExist(countOfRepeatedColor, pieceColor);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		countOfRepeatedColor = 0; 
		// right-down check
		for (int i = row, j = col; i <= Constants.MAX_ROW_NUMBER && j <= Constants.MAX_COL_NUMBER; i++,j++) {
			if (board[i][j] == pieceColor) {
				countOfRepeatedColor++;
			} else {
				break;
			}
		}
		setWinnerIfExist(countOfRepeatedColor, pieceColor);
		
		countOfRepeatedColor--; // to eliminate counting for the same cell twice
		// left-up check
		for (int i = row, j = col; i >= Constants.MIN_ROW_NUMBER && j >= Constants.MIN_COL_NUMBER; i--, j--) {
			if (board[i][j] == pieceColor) {
				countOfRepeatedColor++;
			} else {
				break;
			}
		}
		setWinnerIfExist(countOfRepeatedColor, pieceColor);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		countOfRepeatedColor = 0;
		// left-down check
		for (int i = row, j = col; i <= Constants.MAX_ROW_NUMBER && j >= Constants.MIN_COL_NUMBER ; i++, j--) {
			if (board[i][j] == pieceColor) {
				countOfRepeatedColor++;
			} else {
				break;
			}
		}
		setWinnerIfExist(countOfRepeatedColor, pieceColor);
		
		countOfRepeatedColor--; // to eliminate counting for the same cell twice
		// right-up check
		for (int i = row, j = col; i >= Constants.MIN_ROW_NUMBER && j <= Constants.MAX_COL_NUMBER; i--,j++) {
			if (board[i][j] == pieceColor) {
				countOfRepeatedColor++;
			} else {
				break;
			}
		}
		setWinnerIfExist(countOfRepeatedColor, pieceColor);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		countOfRepeatedColor = 0;
		// right check
		for (int j = col; j <= Constants.MAX_COL_NUMBER ; j++) {
			if (board[row][j] == pieceColor) {
				countOfRepeatedColor++;
			} else {
				break;
			}
		}
		setWinnerIfExist(countOfRepeatedColor, pieceColor);
		
		
		countOfRepeatedColor--; // to eliminate counting for the same cell twice
		// left check
		for (int j = col; j >= Constants.MIN_COL_NUMBER ; j--) {
			if (board[row][j] == pieceColor) {
				countOfRepeatedColor++;
			} else {
				break;
			}
		}
		setWinnerIfExist(countOfRepeatedColor, pieceColor);
	}

	
	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	public Player getPlayer1() {
		return player1;
	}


	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}


	public Player getPlayer2() {
		return player2;
	}


	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public char[][] getBoard() {
		return board;
	}


	public void setBoard(char[][] board) {
		this.board = board;
	}

	public void setWinners(boolean[] winners) {
		this.winners = winners;
	}
	
	public char getNextMove() {
		return nextMove;
	}


	public void setNextMove(char nextMove) {
		this.nextMove = nextMove;
	}
	
	public boolean getGameStatus() {
		return isRunningGame;
	}


	public void setGameStatus(boolean gameStatus) {
		this.isRunningGame = gameStatus;
	}

}
