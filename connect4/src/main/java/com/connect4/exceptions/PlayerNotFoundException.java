package com.connect4.exceptions;

/**
 * Custom exception class for handling player exceptions
 * @author mhamed
 *
 */
public class PlayerNotFoundException extends Exception{
	
	public PlayerNotFoundException(String message) {
		super(message);
	}

}
