package com.revenat.game.gomoku.domain;

import java.util.EventListener;

/**
 * Represents a role responsible for processing specific events
 * that occur during game session.
 * 
 * @author Vitaly Dragun
 *
 */
public interface GameEventListener extends EventListener {
	
	/**
	 * Handle invalid turn position event.
	 */
	void invalidTurnPosition(Position position);
	
	/**
	 * Handle an event that announces player has made a turn.
	 * @param position specific {@link Position} on {@link GameTable}
	 * where the player made a turn in.
	 * @param mark the player's designation mark
	 */
	void turnIsMade(Position position, Mark mark);
	
	/**
	 * Handle and event that announces game is over and the winner
	 * is found.
	 * @param winner winner's designation mark
	 * @param winningCombination positions of the cells 
	 * on the {@link GameTable} that made player a winner.
	 */
	void winnerIsFound(Mark winner, Position[] winningCombination);
	
	/**
	 * Handle an event which announces a game over because of the draw.
	 */
	void draw();
}
