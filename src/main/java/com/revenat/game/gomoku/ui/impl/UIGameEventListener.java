package com.revenat.game.gomoku.ui.impl;

import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.game.gomoku.domain.GameEventListener;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;
import com.revenat.game.gomoku.ui.GameWindow;
import com.revenat.game.gomoku.ui.UserDialogProvider;

/**
 * Handles game events via interactions with {@link SwingGameWindow}
 * 
 * @author Vitaly Dragun
 *
 */
public class UIGameEventListener implements GameEventListener {
	private static final String DIALOG_TITLE = "Game message";

	private static final Logger LOG = LoggerFactory.getLogger(UIGameEventListener.class);
	
	private final GameWindow gameWindow;
	private final UserDialogProvider dialogProvider;

	public UIGameEventListener(GameWindow gameWindow, UserDialogProvider dialogProvider) {
		requireNonNull(gameWindow, "GameWindow can not be null.");
		requireNonNull(dialogProvider, "Dialog provider can not be null.");
		this.gameWindow = gameWindow;
		this.dialogProvider = dialogProvider;
	}

	@Override
	public void invalidTurnPosition(Position position) {
		requireNonNull(position, "Turn position can not be null.");
		
		dialogProvider.showMessageDialog("Cell is not free! Choose another cell", DIALOG_TITLE);
	}

	@Override
	public void turnIsMade(Position position, Mark mark) {
		requireNonNull(position, "Turn position can not be null.");
		requireNonNull(mark, "Mark can not be null.");
		
		gameWindow.renderTableCell(position.ordinal(), mark);
	}

	@Override
	public void winnerIsFound(Mark winner, Position[] winningCombination) {
		requireNonNull(winner, "Winner mark can not be null.");
		requireNonNull(winningCombination, "Winning combination can not be null.");
		
		highlightWinningCombination(winner, winningCombination);
		
		String message = String.format("Game over: %s wins! Play again?", winner);
		boolean isConfirm = dialogProvider.showConfirmationDialog(message, DIALOG_TITLE);
		
		processUserChoice(isConfirm);
	}

	@Override
	public void draw() {
		String message = "Game over: Draw! Play again?";
		boolean isConfirm = dialogProvider.showConfirmationDialog(message, DIALOG_TITLE);
		
		processUserChoice(isConfirm);
	}
	
	private void highlightWinningCombination(Mark winner, Position[] combination) {
		if (combination.length == 0) {
			throw new IllegalArgumentException("Winning combination can not be empty.");
		}
		gameWindow.highlightWinningCombination(winner, combination);
	}
	
	private void processUserChoice(boolean isConfirm) {
		if (isConfirm) {
			LOG.info("User choose to play another Gomoku game session.");
			gameWindow.startNewGame();
		} else {
			LOG.info("User choose to exit Gomoku game. Buy!");
			System.exit(0);
		}
	}


}
