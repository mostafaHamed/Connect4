package com.connect4.exceptions;

/**
 * Custom exception class for handling game room exceptions
 * @author mhamed
 *
 */
public class RoomFilledException extends Exception{

	public RoomFilledException(String message) {
		super(message);
	}

}
