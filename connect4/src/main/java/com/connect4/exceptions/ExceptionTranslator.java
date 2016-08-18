package com.connect4.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * EExceptionTranslator class
 * @author mhamed
 *
 */
@ControllerAdvice(annotations = RestController.class)
public class ExceptionTranslator {

	@ExceptionHandler(RoomFilledException.class)
	@ResponseStatus(HttpStatus.LOCKED)
	@ResponseBody
	public ResponseEntity<?> processRoomFilledException(RoomFilledException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.LOCKED);
	}

	@ExceptionHandler(PlayerNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<?> processPlayerNotFoundException(PlayerNotFoundException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(WrongMoveException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> processWrongMoveException(WrongMoveException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(WrongTurnException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> processWrongTurnException(WrongTurnException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(GameEndedException.class)
	@ResponseStatus(HttpStatus.LOCKED)
	@ResponseBody
	public ResponseEntity<?> processGameEndedException(GameEndedException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.LOCKED);
	}
	
	@ExceptionHandler(GameNotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<?> processGameNotExistException(GameNotExistException ex) {
		return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}