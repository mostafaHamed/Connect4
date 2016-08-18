package com.connect4.model;

import java.io.Serializable;

/**
 * Model class for game player
 * Each player have two properties id and pieceColor
 * @author mhamed
 *
 */
public class Player implements Serializable{

	private static final long serialVersionUID = 2136684686038545036L;

	String id;
	char pieceColor;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public char getPieceColor() {
		return pieceColor;
	}
	
	public void setPieceColor(char pieceColor) {
		this.pieceColor = pieceColor;
	}
	
}
