package com.revenat.game.gomoku.domain.impl;

import com.revenat.game.gomoku.domain.Position;

/**
 * Notifies about situation where given {@link Position} is invalid.
 * 
 * @author Vitaly Dragun
 *
 */
public class InvalidPositionException extends IllegalArgumentException {
	private static final long serialVersionUID = -3179780516853183346L;

	public InvalidPositionException(String s) {
		super(s);
	}

}
