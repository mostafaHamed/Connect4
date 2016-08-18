package com.connect4.exceptions;

/**
 * Custom exception class for handling game wrong moves exceptions
 * @author mhamed
 *
 */
public class WrongMoveException extends Exception{
	
	public WrongMoveException(String message) {
		super(message);
	}

}
