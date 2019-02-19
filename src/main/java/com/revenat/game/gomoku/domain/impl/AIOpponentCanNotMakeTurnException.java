package com.revenat.game.gomoku.domain.impl;

/**
 * Notifies about situation when AI opponent can not find position to make turn to.
 * 
 * @author Vitaly Dragun
 *
 */
public class AIOpponentCanNotMakeTurnException extends RuntimeException {
	private static final long serialVersionUID = 4550942924841666379L;

	public AIOpponentCanNotMakeTurnException(String message) {
		super(message);
	}

}
