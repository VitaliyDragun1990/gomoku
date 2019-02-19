package com.revenat.game.gomoku.domain;

/**
 * Represent a role responsible for determining next turn position 
 * on a {@link GameTable} for an AI player.
 * 
 * @author Vitaly Dragun
 *
 */
public interface AIGameOpponent {

	/**
	 * Determines best possible next turn {@link Position} for
	 * player designated with specified {@link Mark}
	 */
	Position determineNextTurnPositionFor(final Mark playerMark);
}
