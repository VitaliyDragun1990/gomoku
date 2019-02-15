package com.revenat.game.gomoku.domain;

/**
 * A role that represents game mode that can be applied to {@link GameSession}
 * 
 * @author Vitaly Dragun
 *
 */
public interface GameMode {

	/**
	 * Start new game session
	 */
	void startGame();

	/**
	 * Return a {@link Mark} that designates a player
	 * who's going to make a turn.
	 */
	Mark getCurrentPlayer();

	/**
	 * Process result of the turn has been made.
	 */
	void handleTurnResult(boolean isGameOver);

}
