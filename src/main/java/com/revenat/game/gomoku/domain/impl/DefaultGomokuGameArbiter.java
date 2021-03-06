package com.revenat.game.gomoku.domain.impl;

import java.util.Arrays;
import java.util.Objects;

import com.revenat.game.gomoku.domain.GameArbiter;
import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;

/**
 * Default implementation of {@link GameArbiter} specific to
 * Gomoku game.
 * 
 * @author Vitaly Dragun
 *
 */
public class DefaultGomokuGameArbiter implements GameArbiter {
	/**
	 * Represents how much you should add to some {@link Position} {@code ordinal()}
	 * to get next adjacent {@link Position} in some main diagonal on {@link GameTable}
	 */
	private static final int NEXT_IN_MAIN_DIAG_INCREMENT = 16;
	/**
	 * Represents how much you should add to some {@link Position} {@code ordinal()}
	 * to get next adjacent {@link Position} in some secondary diagonal on {@link GameTable}
	 */
	private static final int NEXT_IN_SECOND_DIAG_INCREMENT = 14;
	/**
	 * Represents how much you should add to some {@link Position} {@code ordinal()}
	 * to get next adjacent {@link Position} in some row on {@link GameTable}
	 */
	private static final int NEXT_IN_ROW_INCREMENT = 1;
	/**
	 * Represents how much you should add to some {@link Position} {@code ordinal()}
	 * to get next adjacent {@link Position} in some column on {@link GameTable}
	 */
	private static final int NEXT_IN_COLUMN_INCREMENT = 15;
	/**
	 * Represents how many adjacent cells on {@link GameTable} should be marked
	 * with the same not {@code EMPTY} {@link Mark} to represents a winning
	 * combination
	 */
	private static final int WINNING_COUNT = 5;
	private static final int TABLE_SIZE = 15;
	
	private final GameTable gameTable;
	private final Position[] winningCombination = new Position[WINNING_COUNT];
	private int winningComboCount = 0;
	
	public DefaultGomokuGameArbiter(GameTable gameTable) {
		Objects.requireNonNull(gameTable, "GameTable can not be null.");
		this.gameTable = gameTable;
	}

	@Override
	public CheckResult checkForGameOver() {
		CheckResult result = lookForWinnerByRows();
		if (result.isWinner()) {
			return result;
		}
		result = lookForWinnerByColumns();
		if (result.isWinner()) {
			return result;
		}
		result = lookForWinnerByDiagonals();
		if (result.isWinner()) {
			return result;
		}

		return checkForDraw();
	}
	
	private CheckResult lookForWinnerByRows() {
		for (int row = 0; row < TABLE_SIZE; row++) {
			for (int col = 0; col < 11; col++) {
				Position from = Position.from(row, col);
				Position to = Position.from(row, col+4);
				if (isWinningCombo(from, to)) {
					return winnerFound();
				}
			}
		}
		
		return CheckResult.keepOnPlaying();
	}
	
	private CheckResult lookForWinnerByColumns() {
		for (int col = 0; col < TABLE_SIZE; col++) {
			for (int row = 0; row < 11; row++) {
				Position from = Position.from(row, col);
				Position to = Position.from(row+4, col);
				if (isWinningCombo(from, to)) {
					return winnerFound();
				}
			}
		}
		
		return CheckResult.keepOnPlaying();
	}
	
	private CheckResult lookForWinnerByDiagonals() {
		for (int row = 0; row < 11; row++) {
			for (int col = 0; col < 11; col++) {
				Position upperLeft = Position.from(row, col);
				Position upperRight = Position.from(upperLeft.ordinal() + 4);
				if (hasWinnerByDiagonals(upperLeft, upperRight)) {
					return winnerFound();
				}
			}
		}
		
		return CheckResult.keepOnPlaying();
	}
	
	private CheckResult checkForDraw() {
		for (int ordinal = 1; ordinal <= gameTable.getTotalCells(); ordinal++) {
			if (gameTable.isCellEmpty(Position.from(ordinal))) {
				return CheckResult.keepOnPlaying();
			}
		}
		return CheckResult.draw();
	}
	
	private boolean hasWinnerByDiagonals(Position upperLeft, Position upperRight) {
		Position bottomLeft = Position.from(upperRight.ordinal() + 4 * NEXT_IN_SECOND_DIAG_INCREMENT);
		Position bottomRight = Position.from(upperLeft.ordinal() + 4 * NEXT_IN_MAIN_DIAG_INCREMENT);
		
		// Check by secondary diagonal
		if (isWinningCombo(upperRight, bottomLeft)) {
			return true;
		}
		// Check by main diagonal
		return isWinningCombo(upperLeft, bottomRight);
	}

	private boolean isWinningCombo(Position from, Position to) {
		if (isRowCombination(from, to)) {
			return isWinningCombo(from, NEXT_IN_ROW_INCREMENT);
		} 
		else if (isColumnCombination(from, to)) {
			return isWinningCombo(from, NEXT_IN_COLUMN_INCREMENT);
		} 
		else if (isSecondaryDiagonalCombination(from, to)) {
			return isWinningCombo(from, NEXT_IN_SECOND_DIAG_INCREMENT);
		} 
		else if (isMainDiagonalCombination(from, to)) {
			return isWinningCombo(from, NEXT_IN_MAIN_DIAG_INCREMENT);
		}
		return false;
	}
	
	private boolean isWinningCombo(Position from, int nextPositionIncrement) {
		Position current = Position.from(from.ordinal());
		Position next = null;
		
		for (int i = 1; i < WINNING_COUNT; i++) {
			next = Position.from(current.ordinal() + nextPositionIncrement);
			
			if (!markedBySamePlayer(current, next)) {
				clearWinningCombo();
				return false;
			}
			
			addToWinningCombo(current);
			current = next;
		}
		addToWinningCombo(current);
		return true;
	}
	
	private CheckResult winnerFound() {
		CheckResult result = CheckResult.winner(winningCombination);
		clearWinningCombo();
		return result;
	}
	
	private void addToWinningCombo(Position position) {
		winningCombination[winningComboCount++] = position;
	}
	
	private void clearWinningCombo() {
		Arrays.fill(winningCombination, null);
		winningComboCount = 0;
	}
	
	private boolean markedBySamePlayer(Position posA, Position posB) {
		return cellNotEmpty(posA) && getMarkFor(posA) == getMarkFor(posB);
	}

	private boolean cellNotEmpty(Position position) {
		return !gameTable.isCellEmpty(position);
	}
	
	private Mark getMarkFor(Position position) {
		return gameTable.getCellMark(position);
	}

	private static boolean isSecondaryDiagonalCombination(Position from, Position to) {
		return from.ordinal() + 4 * NEXT_IN_SECOND_DIAG_INCREMENT == to.ordinal();
	}
	
	private static boolean isMainDiagonalCombination(Position from, Position to) {
		return from.ordinal() + 4 * NEXT_IN_MAIN_DIAG_INCREMENT == to.ordinal();
	}

	private static boolean isColumnCombination(Position from, Position to) {
		return from.column() == to.column();
	}

	private static boolean isRowCombination(Position from, Position to) {
		return from.row() == to.row();
	}
}
