package com.revenat.game.gomoku.domain.impl;

import java.util.Objects;
import java.util.Random;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.DifficultyLevel;
import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;

/**
 * Represents default implementation of the AI opponent in Gomoku game which is capable of making the best
 * possible turn against its opponent.
 * 
 * @author Vitaly Dragun
 *
 */
public class DefaultGomokuAIGameOpponent implements AIGameOpponent {
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
	private static final Position NOT_FOUND = Position.from(-1);

	private final GameTable gameTable;
	private DifficultyLevel level = DifficultyLevel.EASY;

	public DefaultGomokuAIGameOpponent(GameTable gameTable) {
		Objects.requireNonNull(gameTable, "GameTable can not be null.");
		this.gameTable = gameTable;
	}
	

	/**
	 * Sets difficulty level for AI opponent.
	 * @param level
	 */
	public void setDifficultylevel(DifficultyLevel level) {
		Objects.requireNonNull(level, "Difficulty level can not be null");
		this.level = level;
	}


	@Override
	public Position determineNextTurnPositionFor(final Mark playerMark) {
		Objects.requireNonNull(playerMark, "playerMark can not be null");
		checkPlayerMark(playerMark);
		
		Position position = findPositionToWin(playerMark);

		if (positionNotFound(position)) {
			position = findPositionToPreventOpponentFromWinning(playerMark);
		}

		if (positionNotFound(position)) {
			position = findPositionToWinOnNextTurns(playerMark);
		}

		if (positionNotFound(position)) {
			position = findRandomFreePosition();
		}

		return position;
	}

	private void checkPlayerMark(Mark playerMark) {
		if (playerMark == Mark.EMPTY) {
			throw new IllegalArgumentException("Player mark should be 'X' or 'O', not 'EMPTY'");
		}
		
	}


	/**
	 * Try to find position to mark on next turn to win immediately.
	 * @param playerMark player for whom this position brings victory.
	 */
	private Position findPositionToWin(final Mark playerMark) {
		int alreadyMarkedCount = 4;
		return findPositionToMarkFor(alreadyMarkedCount, playerMark);
	}

	/**
	 * Try to find position to mark on next turn to prevent opponent
	 * from winning. Try to prevent him from building winning combination.
	 * @param playerMark player for whom this position prevents opponent from winning.
	 */
	private Position findPositionToPreventOpponentFromWinning(final Mark playerMark) {
		int alreadyMarkedCount = level.level();
		Mark opponentMark = playerMark.getOpponent();
		return findPositionToMarkFor(alreadyMarkedCount, opponentMark);
	}
	
	/**
	 * Try to find good position to mark on next turn which can
	 * bring possible victory in the near future.
	 * @param playerMark player for whom this position will be searched.
	 */
	private Position findPositionToWinOnNextTurns(final Mark playerMark) {
		Position toMark = findPositionToMakeFourMarkedSequence(playerMark);
		
		if (positionNotFound(toMark)) {
			toMark = findPositionToMakeThreeMarkedSequence(playerMark);
		}
		
		if (positionNotFound(toMark)) {
			toMark = findPositionToMakeTwoMarkedSequence(playerMark);
		}
		
		return toMark;
	}
	
	/**
	 * Try to find random empty cell position to mark on next turn.
	 */
	private Position findRandomFreePosition() {
		Position[] freeCells = new Position[gameTable.getTotalCells()];
		int count = 0;

		for (int row = 0; row < gameTable.getTotalRows(); row++) {
			for (int col = 0; col < gameTable.getTotalColumns(); col++) {
				Position cellPosition = Position.from(row, col);

				if (gameTable.isCellEmpty(cellPosition)) {
					freeCells[count++] = cellPosition;
				}
			}
		}

		int randomIndex = new Random().nextInt(count);
		return freeCells[randomIndex];
	}
	
	/**
	 * Return position to mark (empty position) where some prerequisites apply.
	 * @param alreadyMarkedCount length of the already marked sequence of cells
	 * @param playerMark specific {@link Mark} which should have been used to mark sequence mentioned above
	 */
	private Position findPositionToMarkFor(int alreadyMarkedCount, Mark playerMark) {
		
		Position position = findPositionToMarkByRows(alreadyMarkedCount, playerMark);

		// Find by columns - winning
		if (positionNotFound(position)) {
			position = findPostitionToMarkByColumns(alreadyMarkedCount, playerMark);
		}

		// Find by diagonals - winning
		if (positionNotFound(position)) {
			position = findPositionToMarkByDiagonals(alreadyMarkedCount, playerMark);
		}

		return position;
	}

	private Position findPositionToMarkByRows(int alreadyMarkedCount, final Mark playerMark) {
		for (int row = 0; row < gameTable.getTotalRows(); row++) {
			
			Position position = findPositionToMarkByRow(row, alreadyMarkedCount, playerMark);
			if (positionIsFound(position)) {
				return position;
			}
		}
		return NOT_FOUND;
	}

	private Position findPositionToMarkByRow(int row, int alreadyMarkedCount, final Mark playerMark) {
		int threshold = getThresholdForMarkedCount(alreadyMarkedCount);
		
		for (int col = 0; col < threshold; col++) {
			Position from = Position.from(row, col);
			Position to = Position.from(from.ordinal() + alreadyMarkedCount);
			Position toMark = findPositionToMark(from, to, alreadyMarkedCount, playerMark);
			if (positionIsFound(toMark)) {
				return toMark;
			}
		}
		return NOT_FOUND;
	}
	
	private Position findPostitionToMarkByColumns(int alreadyMarkedCount, final Mark playerMark) {
		for (int col = 0; col < gameTable.getTotalColumns(); col++) {
			
			Position position = findPositionToMarkByColumn(col, alreadyMarkedCount, playerMark);
			if (positionIsFound(position)) {
				return position;
			}
		}
		return NOT_FOUND;
	}
	
	private Position findPositionToMarkByColumn(int col, int alreadyMarkedcount, final Mark playerMark) {
		int threshold = getThresholdForMarkedCount(alreadyMarkedcount);
		
		for (int row = 0; row < threshold; row++) {
			Position from = Position.from(row, col);
			Position to = Position.from(from.ordinal() + 15 * alreadyMarkedcount);
			Position toMark = findPositionToMark(from, to, alreadyMarkedcount, playerMark);
			if (positionIsFound(toMark)) {
				return toMark;
			}
		}
		
		return NOT_FOUND;
	}
	
	private Position findPositionToMarkByDiagonals(int alreadyMarkedcount, Mark playerMark) {
		int threshold = getThresholdForMarkedCount(alreadyMarkedcount);
		
		for (int row = 0; row < threshold; row ++) {
			for (int col = 0; col < threshold; col++) {
				Position upperLeft = Position.from(row, col);
				Position upperRight = Position.from(upperLeft.ordinal() + alreadyMarkedcount);
				Position toMark = checkDiagonals(upperLeft, upperRight, alreadyMarkedcount, playerMark);
				if (positionIsFound(toMark)) {
					return toMark;
				}
			}
		}
		return NOT_FOUND;
	}

	private Position checkDiagonals(Position upperLeft, Position upperRight, int alreadyMarkedCount, Mark playerMark) {
		Position bottomLeft = Position.from(upperRight.ordinal() + NEXT_IN_SECOND_DIAG_INCREMENT * alreadyMarkedCount);
		Position bottomRight = Position.from(upperLeft.ordinal() + NEXT_IN_MAIN_DIAG_INCREMENT * alreadyMarkedCount);
		
		// Try to find by secondary diagonal
		Position toMark = findPositionToMark(upperRight, bottomLeft, alreadyMarkedCount, playerMark);
		if (positionIsFound(toMark)) {
			return toMark;
		}
		// Try to find by main diagonal
		return findPositionToMark(upperLeft, bottomRight, alreadyMarkedCount, playerMark);
	}

	private Position findPositionToMark(Position from, Position to, int alreadyMarkedCount, Mark playerMark) {
		if (isRowCombination(from, to)) {
			return findPositionByRow(from, to, alreadyMarkedCount, playerMark);
		}
		else if (isColumnCombination(from, to)) {
			return findPositionByColumn(from, to, alreadyMarkedCount, playerMark);
		}
		else if (isSecondaryDiagonalCombination(from, to)) {
			return findPositionByDiagonal(from, alreadyMarkedCount, playerMark, NEXT_IN_SECOND_DIAG_INCREMENT);
		}
		else if (isMainDiagonalCombination(from, to)) {
			return findPositionByDiagonal(from, alreadyMarkedCount, playerMark, NEXT_IN_MAIN_DIAG_INCREMENT);
		}
		
		return NOT_FOUND;
	}
	
	private Position findPositionByDiagonal(Position from, int alreadyMarkedCount, Mark playerMark, int increment) {
		int markedCellsCount = 0;
		Position emptyPosition = NOT_FOUND;
		Position current = null;
		Position next = from;
		
		for (int i = 0; i <= alreadyMarkedCount; i++) {
			current = next;
			if (gameTable.isCellMarked(current, playerMark)) {
				markedCellsCount++;
			} else if (gameTable.isCellEmpty(current)) {
				emptyPosition = current;
			}
			next = Position.from(next.ordinal() + increment);
		}
		return tryToFindPositionToMark(markedCellsCount, alreadyMarkedCount, emptyPosition);
	}

	private Position findPositionByColumn(Position from, Position to, int alreadyMarkedCount, Mark playerMark) {
		int markedCellsCount = 0;
		Position emptyPosition = NOT_FOUND;
		int col = from.column();
		
		for (int row = from.row(); row <= to.row(); row++) {
			Position position = Position.from(row, col);
			
			if (gameTable.isCellMarked(position, playerMark)) {
				markedCellsCount++;
			} else if (gameTable.isCellEmpty(position)) {
				emptyPosition = Position.from(row, col);
			}
		}
		return tryToFindPositionToMark(markedCellsCount, alreadyMarkedCount, emptyPosition);
	}

	private Position findPositionByRow(Position from, Position to, int alreadyMarkedCount, Mark playerMark) {
		int markedCellsCount = 0;
		Position emptyPosition = NOT_FOUND;
		int row = from.row();
		
		for (int col = from.column(); col <= to.column(); col++) {
			Position position = Position.from(row, col);
			
			if (gameTable.isCellMarked(position, playerMark)) {
				markedCellsCount++;
			} else if (gameTable.isCellEmpty(position)) {
				emptyPosition = Position.from(row, col);
			}
		}
		return tryToFindPositionToMark(markedCellsCount, alreadyMarkedCount, emptyPosition);
	}

	private Position tryToFindPositionToMark(int markedCellsCount, int alreadyMarkedCount, Position emptyPosition) {
		if (markedCellsCount == alreadyMarkedCount && positionIsFound(emptyPosition)) {
			return emptyPosition;
		}
		return NOT_FOUND;
	}
	
	private Position findPositionToMakeFourMarkedSequence(final Mark playerMark) {
		return findPositionToMarkFor(3, playerMark);
	}
	
	private Position findPositionToMakeThreeMarkedSequence(final Mark playerMark) {
		return findPositionToMarkFor(2, playerMark);
	}

	private Position findPositionToMakeTwoMarkedSequence(final Mark playerMark) {
		for (int row = 0; row < gameTable.getTotalRows(); row++) {
			for (int col = 0; col < gameTable.getTotalColumns(); col++) {
				Position cellPosition = Position.from(row, col);

				if (gameTable.isCellMarked(cellPosition, playerMark)) {
					Position[] possiblePositions = getPossibleTurnPositions(cellPosition);
					int randomIndex = new Random().nextInt(possiblePositions.length);

					for (int option = 0; option < possiblePositions.length; option++) {
						Position possiblePosition = possiblePositions[randomIndex];
						if (gameTable.isCellEmpty(possiblePosition)) {
							return possiblePosition;
						}
						randomIndex++;
						randomIndex %= possiblePositions.length;
					}
				}
			}
		}

		return NOT_FOUND;
	}

	private Position[] getPossibleTurnPositions(Position cellPosition) {
		return gameTable.getAdjacentCellsPositionsFor(cellPosition);
	}
	
	private int getThresholdForMarkedCount(int alreadyMarkedCount) {
		if (alreadyMarkedCount == 4) {
			return 11;
		} else if (alreadyMarkedCount == 3) {
			return 12;
		} else if (alreadyMarkedCount == 2) {
			return 13;
		}
		return 0;
	}

	private boolean positionNotFound(Position position) {
		return !position.isValid();
	}

	private boolean positionIsFound(Position position) {
		return position.isValid();
	}
	
	private boolean isSecondaryDiagonalCombination(Position from, Position to) {
		return (to.ordinal() - from.ordinal()) % 14 == 0;
	}
	
	private boolean isMainDiagonalCombination(Position from, Position to) {
		return (to.ordinal() - from.ordinal()) % 16 == 0;
	}

	private boolean isColumnCombination(Position from, Position to) {
		return from.column() == to.column();
	}

	private boolean isRowCombination(Position from, Position to) {
		return from.row() == to.row();
	}
}
