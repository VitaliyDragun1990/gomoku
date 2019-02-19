package com.revenat.game.gomoku.domain.impl;

import java.util.Objects;

import com.revenat.game.gomoku.domain.GameArbiter;
import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;
import com.revenat.game.gomoku.infra.DataSet;
import com.revenat.game.gomoku.infra.DynaArray;

/**
 * Improved implementation of {@link GameArbiter} specific to Gomoku game.
 * Inspired by implementation presented at DevStudy.net lecture 06 in the Http
 * Server course.
 * 
 * @author Vitaly Dragun
 *
 */
public class ImprovedGomokuGameArbiter implements GameArbiter {
	/**
	 * Represents how many adjacent cells on {@link GameTable} should be marked with
	 * the same not {@code EMPTY} {@link Mark} to represents a winning combination
	 */
	private static final int WINNING_COUNT = 5;
	private static final int TABLE_SIZE = 15;

	private final GameTable gameTable;

	public ImprovedGomokuGameArbiter(GameTable gameTable) {
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
			DataSet<Position> positions = new DynaArray<>();
			Position previous = Position.from(row, 0);
			positions.add(previous);
			for (int col = 1; col < TABLE_SIZE; col++) {
				Position current = Position.from(row, col);
				if (markedByTheSamePlayer(previous, current)) {
					positions.add(current);
					if (positions.size() == WINNING_COUNT) {
						return winnerFound(positions);
					}
				} else {
					positions.clear();
					positions.add(current);
					if (col > TABLE_SIZE - WINNING_COUNT) {
						break;
					}
				}
				previous = current;
			}
		}

		return CheckResult.keepOnPlaying();
	}

	private CheckResult lookForWinnerByColumns() {
		for (int col = 0; col < TABLE_SIZE; col++) {
			DataSet<Position> positions = new DynaArray<>();
			Position previous = Position.from(0, col);
			positions.add(previous);
			for (int row = 1; row < TABLE_SIZE; row++) {
				Position current = Position.from(row, col);
				if (markedByTheSamePlayer(previous, current)) {
					positions.add(current);
					if (positions.size() == WINNING_COUNT) {
						return winnerFound(positions);
					}
				} else {
					positions.clear();
					positions.add(current);
					if (row > TABLE_SIZE - WINNING_COUNT) {
						break;
					}
				}
				previous = current;
			}
		}

		return CheckResult.keepOnPlaying();
	}

	private CheckResult lookForWinnerByDiagonals() {
		CheckResult checkResult = lookForWinnerByMainDiagonal();
		if (!checkResult.isWinner()) {
			checkResult = lookForWinnerBySecondaryDiagonal();
		}

		return checkResult;
	}

	private CheckResult lookForWinnerByMainDiagonal() {
		int winningCountMinus1 = WINNING_COUNT - 1;

		for (int row = 0; row < TABLE_SIZE - winningCountMinus1; row++) {
			for (int col = 0; col < TABLE_SIZE - winningCountMinus1; col++) {
				Position previous = Position.from(row, col);
				DataSet<Position> positions = new DynaArray<>();
				positions.add(previous);
				for (int increment = 1; increment < WINNING_COUNT; increment++) {
					Position current = Position.from(row + increment, col + increment);
					if (markedByTheSamePlayer(previous, current)) {
						positions.add(current);
						if (positions.size() == WINNING_COUNT) {
							return winnerFound(positions);
						}
					} else {
						break;
					}
					previous = current;
				}
			}
		}

		return CheckResult.keepOnPlaying();
	}

	private CheckResult lookForWinnerBySecondaryDiagonal() {
		int winningCountMinus1 = WINNING_COUNT - 1;

		for (int row = 0; row < TABLE_SIZE - winningCountMinus1; row++) {
			for (int col = winningCountMinus1; col < TABLE_SIZE; col++) {
				Position previous = Position.from(row, col);
				DataSet<Position> positions = new DynaArray<>();
				positions.add(previous);
				for (int increment = 1; increment < WINNING_COUNT; increment++) {
					Position current = Position.from(row + increment, col - increment);
					if (markedByTheSamePlayer(previous, current)) {
						positions.add(current);
						if (positions.size() == WINNING_COUNT) {
							return winnerFound(positions);
						}
					} else {
						break;
					}
					previous = current;
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

	private CheckResult winnerFound(DataSet<Position> winningCombo) {
		return CheckResult.winner(winningCombo.toArray(new Position[5]));
	}

	private boolean markedByTheSamePlayer(Position posA, Position posB) {
		return cellNotEmpty(posA) && getMarkFor(posA) == getMarkFor(posB);
	}

	private boolean cellNotEmpty(Position position) {
		return !gameTable.isCellEmpty(position);
	}

	private Mark getMarkFor(Position position) {
		return gameTable.getCellMark(position);
	}
}
