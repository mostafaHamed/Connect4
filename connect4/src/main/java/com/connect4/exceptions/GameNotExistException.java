package com.connect4.exceptions;

/**
 * Custom exception class for handling game exceptions
 * @author mhamed
 *
 */
public class GameNotExistException extends Exception{
	
	public GameNotExistException(String message){
		super(message);
	}
}
