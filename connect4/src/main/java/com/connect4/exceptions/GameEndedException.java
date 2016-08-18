package com.connect4.exceptions;

/**
 * Custom exception class for handling game exceptions
 * @author mhamed
 *
 */
public class GameEndedException extends Exception{
	
	public GameEndedException(String message){
		super(message);
	}

}
