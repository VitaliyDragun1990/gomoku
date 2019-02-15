package com.revenat.game.gomoku.domain;

/**
 * Represents a role responsible for managing game session.
 * @author Vitaly Dragun
 *
 */
public interface GameSession {

	/**
	 * Add listener for handling specific game events.
	 */
	void addListener(GameEventListener listener);
	
	
	/**
	 * Set specified {@link GameMode}
	 */
	void setGameMode(GameMode gameMode);
	
	/**
	 * Start a new game session.
	 */
	public void startNewGame();
	
	/**
	 * Process player turn at specified {@link Position}
	 */
	public void processPlayerTurn(Position position);
}
