package com.revenat.game.gomoku.domain.impl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;

/**
 * Represents improved implementation of the AI opponent in Gomoku game which is capable of making the best
 * possible turn against its opponent. Inspired by lecture 06 in the Http Server course by DevStudy.net
 * 
 * @author Vitaly Dragun
 *
 */
public class ImprovedGomokuAIGameOpponent implements AIGameOpponent {
	private static final Logger LOG = LoggerFactory.getLogger(ImprovedGomokuAIGameOpponent.class);
	
	private static final int WINNING_COUNT = 5;
	private static final int TABLE_SIZE = 15;
	private static final Position NOT_FOUND = Position.from(-1);

	private final GameTable gameTable;

	public ImprovedGomokuAIGameOpponent(GameTable gameTable) {
		requireNonNull(gameTable, "GameTable can not be null.");
		this.gameTable = gameTable;
	}

	@Override
	public Position determineNextTurnPositionFor(final Mark playerMark) {
		requireNonNull(playerMark, "playerMark can not be null");
		checkPlayerMark(playerMark);
		LOG.trace("AI opponent '{}' tries to find next turn position", playerMark);
		
		Mark[] players = {playerMark, playerMark.getOpponent()};
		
		for (int notEmptyCount = WINNING_COUNT-1; notEmptyCount > 0; notEmptyCount--) {
			for (Mark player : players) {
				Position position = tryMakeTurn(player, notEmptyCount);
				if (positionIsFound(position)) {
					return position;
				}
			}
		}

		return findRandomEmptyPosition();
	}

	private Position tryMakeTurn(Mark player, int notEmptyCount) {
		LOG.trace("AI opponent tries to find a free position among {} cell(s) already marked with {}",
				notEmptyCount, player);
		
		Position position = tryMakeTurnByRow(player, notEmptyCount);
		if (positionIsFound(position)) {
			LOG.debug("AI opponent finds a free position {} in a row among {} cell(s) marked with {}",
					position, notEmptyCount, player);
			return position;
		}
		position = tryMakeTurnByColumn(player, notEmptyCount);
		if (positionIsFound(position)) {
			LOG.debug("AI opponent finds a free position {} in a column among {} cell(s) marked with {}",
					position, notEmptyCount, player);
			return position;
		}
		position = tryMakeTurnBySecondaryDiagonal(player, notEmptyCount);
		if (positionIsFound(position)) {
			LOG.debug("AI opponent finds a free position {} in a secondary diagonal among {} cell(s) marked with {}",
					position, notEmptyCount, player);
			return position;
		}
		position = tryMakeTurnByMainDiagonal(player, notEmptyCount);
		if (positionIsFound(position)) {
			LOG.debug("AI opponent finds a free position {} in a main diagonal among {} cell(s) marked with {}",
					position, notEmptyCount, player);
			return position;
		}
		
		LOG.trace("AI opponent failes to find any free position among {} cell(s) marked with {}", notEmptyCount, player);
		return NOT_FOUND;
	}


	private Position tryMakeTurnByRow(Mark player, int notEmptyCount) {
		LOG.trace("AI opponent tries to find a free position in row among {} cell(s) already marked with {}",
				notEmptyCount, player);
		
		for (int row = 0; row < TABLE_SIZE; row++) {
			for (int col = 0; col < TABLE_SIZE - WINNING_COUNT - 1; col++) {
				boolean hasEmptyCells = false;
				int matchedCount = 0;
				List<Position> inspectedCells = new ArrayList<>();
				for (int increment = 0; increment < WINNING_COUNT; increment++) {
					Position position = Position.from(row, col + increment);
					inspectedCells.add(position);
					if (isMarkedWith(position, player)) {
						matchedCount++;
					} else if (isEmpty(position)) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (matchedCount == notEmptyCount && hasEmptyCells) {
					return findEmptyPositionForTurn(inspectedCells);
				}
			}
		}
		
		LOG.trace("AI opponent failes to find a free position in row among {} cell(s) already marked with {}",
				notEmptyCount, player);
		return NOT_FOUND;
	}
	
	private Position tryMakeTurnByColumn(Mark player, int notEmptyCount) {
		LOG.trace("AI opponent tries to find a free position in column among {} cell(s) already marked with {}",
				notEmptyCount, player);
		
		for (int col = 0; col < TABLE_SIZE; col++) {
			for (int row = 0; row < TABLE_SIZE - WINNING_COUNT - 1; row++) {
				boolean hasEmptyCells = false;
				int matchedCount = 0;
				List<Position> inspectedCells = new ArrayList<>();
				for (int increment = 0; increment < WINNING_COUNT; increment++) {
					Position position = Position.from(row + increment, col);
					inspectedCells.add(position);
					if (isMarkedWith(position, player)) {
						matchedCount++;
					} else if (isEmpty(position)) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (matchedCount == notEmptyCount && hasEmptyCells) {
					return findEmptyPositionForTurn(inspectedCells);
				}
			}
		}
		
		LOG.trace("AI opponent failes to find a free position in row among {} cell(s) already marked with {}",
				notEmptyCount, player);
		return NOT_FOUND;
	}
	
	private Position tryMakeTurnByMainDiagonal(Mark player, int notEmptyCount) {
		LOG.trace("AI opponent tries to find a free position in main diagonal among {} cell(s) already marked with {}",
				notEmptyCount, player);
		
		for (int row = 0; row < TABLE_SIZE - WINNING_COUNT - 1; row++) {
			for (int col = 0; col < TABLE_SIZE - WINNING_COUNT - 1; col++) {
				boolean hasEmptyCells = false;
				int matchedCount = 0;
				List<Position> inspectedCells = new ArrayList<>();
				for (int increment = 0; increment < WINNING_COUNT; increment++) {
					Position position = Position.from(row + increment, col + increment);
					inspectedCells.add(position);
					if (isMarkedWith(position, player)) {
						matchedCount++;
					} else if (isEmpty(position)) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (matchedCount == notEmptyCount && hasEmptyCells) {
					return findEmptyPositionForTurn(inspectedCells);
				}
			}
		}
		
		LOG.trace("AI opponent failes to find a free position in main diagonal among {} cell(s) already marked with {}",
				notEmptyCount, player);
		return NOT_FOUND;
	}
	
	private Position tryMakeTurnBySecondaryDiagonal(Mark player, int notEmptyCount) {
		LOG.trace("AI opponent tries to find a free position in secondary diagonal among {} cell(s) already marked with {}",
				notEmptyCount, player);
		
		for (int row = 0; row < TABLE_SIZE - WINNING_COUNT - 1; row++) {
			for (int col = WINNING_COUNT - 1; col < TABLE_SIZE; col++) {
				boolean hasEmptyCells = false;
				int matchedCount = 0;
				List<Position> inspectedCells = new ArrayList<>();
				for (int increment = 0; increment < WINNING_COUNT; increment++) {
					Position position = Position.from(row + increment, col - increment);
					inspectedCells.add(position);
					if (isMarkedWith(position, player)) {
						matchedCount++;
					} else if (isEmpty(position)) {
						hasEmptyCells = true;
					} else {
						hasEmptyCells = false;
						break;
					}
				}
				if (matchedCount == notEmptyCount && hasEmptyCells) {
					return findEmptyPositionForTurn(inspectedCells);
				}
			}
		}
		
		LOG.trace("AI opponent failes to find a free position in secondary diagonal among {} cell(s) already marked with {}",
				notEmptyCount, player);
		return NOT_FOUND;
	}

	private Position findEmptyPositionForTurn(List<Position> positions) {
		
		for (int i = 0; i < positions.size(); i++) {
			Position current = positions.get(i);
			if (!isEmpty(current)) {
				if (i == 0) {
					Position next = positions.get(i + 1);
					if (isEmpty(next)) {
						return next;
					}
				} else if (i == positions.size() - 1) {
					Position previous = positions.get(i - 1);
					if (isEmpty(previous)) {
						return previous;
					}
				} else {
					boolean searchDirectionAsc = new Random().nextBoolean();
					int first = searchDirectionAsc ? i + 1 : i - 1;
					int second = searchDirectionAsc ? i - 1 : i + 1;
					if (isEmpty(positions.get(first))) {
						return positions.get(first);
					} else if (isEmpty(positions.get(second))) {
						return positions.get(second);
					}
				}
			}
		}
		
		return NOT_FOUND;
	}


	private boolean isMarkedWith(Position position, Mark player) {
		return gameTable.isCellMarked(position, player);
	}

	private boolean isEmpty(Position position) {
		return gameTable.isCellEmpty(position);
	}

	private void checkPlayerMark(Mark playerMark) {
		if (playerMark == Mark.EMPTY) {
			throw new IllegalArgumentException("Player mark should be 'X' or 'O', not 'EMPTY'");
		}
		
	}
	
	/**
	 * Try to find random empty cell position to mark on next turn.
	 */
	private Position findRandomEmptyPosition() {
		LOG.trace("AI opponent tries to find a random free position to make a turn to");
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
		
		if (count > 0) {
			int randomIndex = new Random().nextInt(count);
			LOG.debug("AI opponent finds random position {} to make a turn to.", freeCells[randomIndex]);
			return freeCells[randomIndex];
		}
		LOG.trace("AI opponent failes to find a random free position to make a turn to");
		return NOT_FOUND;
	}

	private boolean positionIsFound(Position position) {
		return position.isValid();
	}
}
