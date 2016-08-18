package com.connect4.exceptions;

/**
 * Custom exception class for handling game wrong turns exceptions
 * @author mhamed
 *
 */
public class WrongTurnException extends Exception{
	
	public WrongTurnException(String message){
		super(message);
	}

}
