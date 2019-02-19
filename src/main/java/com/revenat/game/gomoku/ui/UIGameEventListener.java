package com.revenat.game.gomoku.ui;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Color;
import java.util.Objects;

import com.revenat.game.gomoku.domain.GameEventListener;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;

/**
 * Handles game events via interactions with {@link GameWindow}
 * 
 * @author Vitaly Dragun
 *
 */
public class UIGameEventListener implements GameEventListener {
	private final GameWindow gameWindow;

	public UIGameEventListener(GameWindow gameWindow) {
		Objects.requireNonNull(gameWindow, "GameWindow can not be null.");
		this.gameWindow = gameWindow;
	}

	@Override
	public void invalidTurnPosition(Position position) {
		Objects.requireNonNull(position, "Turn position can not be null.");
		
		showMessageDialog(gameWindow, "Cell is not free! Choose another cell", gameWindow.getTitle(),
				INFORMATION_MESSAGE);
	}

	@Override
	public void turnIsMade(Position position, Mark mark) {
		Objects.requireNonNull(position, "Turn position can not be null.");
		Objects.requireNonNull(mark, "Mark can not be null.");
		
		gameWindow.renderTableCell(position.ordinal(), mark);
	}

	@Override
	public void winnerIsFound(Mark winner, Position[] winningCombination) {
		Objects.requireNonNull(winner, "Winner mark can not be null.");
		Objects.requireNonNull(winningCombination, "Winning combination can not be null.");
		
		highlightWinningCombination(winner, winningCombination);
		
		String message = String.format("Game over: %s wins! Play again?", winner);
		int userChoice = showConfirmDialog(gameWindow, message, gameWindow.getTitle(),
				YES_NO_OPTION, INFORMATION_MESSAGE);
		
		processUserChoice(userChoice);
	}

	@Override
	public void draw() {
		String message = "Game over: Draw! Play again?";
		int userChoice = showConfirmDialog(gameWindow, message, gameWindow.getTitle(),
				YES_NO_OPTION, INFORMATION_MESSAGE);
		
		processUserChoice(userChoice);
	}
	
	private void highlightWinningCombination(Mark winner, Position[] combination) {
		if (combination.length == 0) {
			throw new IllegalArgumentException("Winning combination can not be empty.");
		}
		
		for (Position position : combination) {
			gameWindow.render(position.ordinal(), winner.toString(), Color.RED);
		}
	}
	
	private void processUserChoice(int userChoice) {
		if (userChoice == YES_OPTION) {
			gameWindow.startNewGameSession();
		} else {
			System.exit(0);
		}
	}


}
