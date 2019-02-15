package com.revenat.game.gomoku.domain;

import java.util.Arrays;

/**
 * Implementation of {@link GameArbiter} specific to
 * Gomoku game.
 * 
 * @author Vitaly Dragun
 *
 */
public class GomokuGameArbiter implements GameArbiter {
	private static final int TABLE_SIZE = 15;
	
	private final GameTable gameTable;
	private final Position[] winningCombination = new Position[5];
	private int count = 0;
	
	public GomokuGameArbiter(GameTable gameTable) {
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
		Position bottomLeft = Position.from(upperRight.ordinal() + 56);
		Position bottomRight = Position.from(upperLeft.ordinal() + 64);
		
		// Check by secondary diagonal
		if (isWinningCombo(upperRight, bottomLeft)) {
			return true;
		}
		// Check by main diagonal
		return isWinningCombo(upperLeft, bottomRight);
	}

	private boolean isWinningCombo(Position from, Position to) {
		if (isRowCombination(from, to)) {
			return isWinningRowCombo(from, to);
		} 
		else if (isColumnCombination(from, to)) {
			return isWinningColumnCombo(from, to);
		} 
		else if (isSecondaryDiagonalCombination(from, to)) {
			return isWinningSecondaryDiagonalCombo(from, to);
		} 
		else if (isMainDiagonalCombination(from, to)) {
			return isWinningMainDiagonalCombo(from, to);
		}
		return false;
	}
	
	private boolean isWinningColumnCombo(Position from, Position to) {
		int row = from.row();
		int col = from.column();
		Position current = Position.from(row, col);
		Position next = null;
		
		for (int i = 0; i < 4; i++) {
			next = Position.from(current.ordinal() + 15);
			
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

	private boolean isWinningRowCombo(Position from, Position to) {
		int row = from.row();
		int col = from.column();
		Position current = Position.from(row, col);
		Position next = null;
				
		for (int i = 0; i < 4; i++) {
			next = Position.from(current.ordinal() + 1);
			
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
	
	private boolean isWinningSecondaryDiagonalCombo(Position from, Position to) {
		Position current = Position.from(from.ordinal());
		Position next = null;
		
		for (int i = 0; i < 4; i++) {
			next = Position.from(current.ordinal() + 14);
			
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
	
	private boolean isWinningMainDiagonalCombo(Position from, Position to) {
		Position current = Position.from(from.ordinal());
		Position next = null;
		
		for (int i = 0; i < 4; i++) {
			next = Position.from(current.ordinal() + 16);
			
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
		winningCombination[count++] = position;
	}
	
	private void clearWinningCombo() {
		Arrays.fill(winningCombination, null);
		count = 0;
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

	private boolean isSecondaryDiagonalCombination(Position from, Position to) {
		return from.ordinal() + 56 == to.ordinal();
	}
	
	private boolean isMainDiagonalCombination(Position from, Position to) {
		return from.ordinal() + 64 == to.ordinal();
	}

	private boolean isColumnCombination(Position from, Position to) {
		return from.column() == to.column();
	}

	private boolean isRowCombination(Position from, Position to) {
		return from.row() == to.row();
	}
}
