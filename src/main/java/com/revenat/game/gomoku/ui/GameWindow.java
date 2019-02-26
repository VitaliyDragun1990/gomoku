package com.revenat.game.gomoku.ui;

import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;

/**
 * Represents game window with graphical game table for abstract game.
 * 
 * @author Vitaly Dragun
 *
 */
public interface GameWindow {

	/**
	 * Starts new game session
	 */
	void startNewGame();
	
	/**
	 * Renders specific mark in table cell at specific position
	 */
	void renderTableCell(int position, Mark mark);
	
	/**
	 * Highlights in some way winning combination
	 */
	void highlightWinningCombination(Mark winner, Position[] winningCombination);
}
