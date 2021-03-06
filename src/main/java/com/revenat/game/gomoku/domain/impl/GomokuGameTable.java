package com.revenat.game.gomoku.domain.impl;

import java.util.Objects;

import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;

/**
 * Represents game table (grid) for 15x15 Gomoku game.
 * 
 * @author Vitaly Dragun
 *
 */
public class GomokuGameTable implements GameTable {
	private static final int TOTAL_ROWS = 15;
	private static final int TOTAL_COLUMNS = 15;
	private static final int TOTAL_CELLS = 225;
	
	private final Mark[][] gameTable = new Mark[TOTAL_ROWS][TOTAL_COLUMNS];
	
	/**
	 * Constructs game table with specific number of rows
	 * and columns
	 */
	public GomokuGameTable() {	
		buildEmptyGameTable();
	}

	private void buildEmptyGameTable() {
		for (int row = 0; row < TOTAL_ROWS; row++) {
			for (int column = 0; column < TOTAL_COLUMNS; column++) {
				gameTable[row][column] = Mark.EMPTY;
			}
		}
	}

	@Override
	public void clear() {
		buildEmptyGameTable();
	}
	
	@Override
	public boolean isCellEmpty(Position position) {
		checkPosition(position);
		
		return getCellAt(position) == Mark.EMPTY;
	}
	
	private static void checkPosition(Position position) {
		Objects.requireNonNull(position,  "Position can not be null");
		if (!position.isValid()) {
			throw new InvalidPositionException("Position is invalid: " + position +
					". Valid coordinates: row 0 - 14, columns 0 - 14");
		}
	}
	
	@Override
	public boolean isCellMarked(Position position, Mark mark) {
		checkPosition(position);
		Objects.requireNonNull(mark,  "Mark to check can not be null");
		
		return getCellAt(position) == mark;
	}
	
	private Mark getCellAt(Position position) {
		return gameTable[position.row()][position.column()];
	}
	
	@Override
	public void markCellAt(Position position, Mark mark) {
		checkPosition(position);
		Objects.requireNonNull(mark,  "Mark can not be null");
		
		gameTable[position.row()][position.column()] = mark;
	}
	
	@Override
	public Mark getCellMark(Position position) {
		checkPosition(position);
		return getCellAt(position);
	}

	@Override
	public Position[] getAdjacentCellsPositionsFor(Position position) {
		Objects.requireNonNull(position,  "Position can not be null");
		
		if (!position.isValid()) {
			return new Position[0];
		}
		
		if (isCornerCell(position)) {
			return getAdjacentCellsPositionsForCornerCell(position);
		}
		else if (isPerimeterCell(position)) {
			return getAdjacentCellsPositionsForPerimeterCell(position);
		}
		else {
			return getAdjacentCellsPositionsForInnerCell(position);
		}
	}

	@Override
	public int getTotalRows() {
		return TOTAL_ROWS;
	}

	@Override
	public int getTotalColumns() {
		return TOTAL_COLUMNS;
	}

	@Override
	public int getTotalCells() {
		return TOTAL_CELLS;
	}

	private static Position[] getAdjacentCellsPositionsForCornerCell(Position position) {
		if (position.ordinal() == 1) {
			return new Position[] { Position.from(2), Position.from(17), Position.from(16) };
		} 
		else if (position.ordinal() == 15) {
			return new Position[] { Position.from(14), Position.from(29), Position.from(30) };
		} 
		else if (position.ordinal() == 211) {
			return new Position[] { Position.from(196), Position.from(197), Position.from(212) };
		}
		
		return new Position[] { Position.from(210), Position.from(209), Position.from(224) };
	}
	
	private static Position[] getAdjacentCellsPositionsForPerimeterCell(Position position) {
		int ordinal = position.ordinal();
		if (isTopPerimeterCell(position)) {
			return new Position[] {Position.from(ordinal-1), Position.from(ordinal+1), Position.from(ordinal+14),
								   Position.from(ordinal+15), Position.from(ordinal+16)};
		} else if (isBottomPerimeterCell(position)) {
			return new Position[] {Position.from(ordinal-1), Position.from(ordinal+1), Position.from(ordinal-16),
								   Position.from(ordinal-15), Position.from(ordinal-14)};
		} else if (isLeftPerimeterCell(position)) {
			return new Position[] {Position.from(ordinal-15), Position.from(ordinal+15), Position.from(ordinal-14),
								   Position.from(ordinal+1), Position.from(ordinal+16)};
		} else {
			return new Position[] {Position.from(ordinal-15), Position.from(ordinal+15), Position.from(ordinal-16),
								   Position.from(ordinal-1), Position.from(ordinal+14)};
		}
	}
	
	private static Position[] getAdjacentCellsPositionsForInnerCell(Position position) {
		int ordinal = position.ordinal();
		return new Position[] {Position.from(ordinal-16), Position.from(ordinal-15), Position.from(ordinal-14),
							   Position.from(ordinal+1), Position.from(ordinal+16), Position.from(ordinal+15),
							   Position.from(ordinal+14), Position.from(ordinal-1)};
	}

	private static boolean isCornerCell(Position position) {
		int ordinal = position.ordinal();
		return ordinal == 1 || ordinal == 15
				|| ordinal == 211 || ordinal == 225;
	}
	
	private static boolean isPerimeterCell(Position position) {
		return isTopPerimeterCell(position) || isBottomPerimeterCell(position)
				|| isLeftPerimeterCell(position) || isRightPerimeterCell(position);
	}

	private static boolean isTopPerimeterCell(Position position) {
		int ordinal = position.ordinal();
		return ordinal >= 2 && ordinal <= 14;
	}
	
	private static boolean isBottomPerimeterCell(Position position) {
		int ordinal = position.ordinal();
		return ordinal >= 212 && ordinal <= 224;
	}
	
	private static boolean isLeftPerimeterCell(Position position) {
		int[] ordinals = {16, 31, 46, 61, 76, 91, 106, 121, 136, 151, 166, 181, 196};
		int ordinal = position.ordinal();
		for (int perimeterOrdinal : ordinals) {
			if (ordinal == perimeterOrdinal) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isRightPerimeterCell(Position position) {
		int[] ordinals = {30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 180, 195, 210};
		int ordinal = position.ordinal();
		for (int perimeterOrdinal : ordinals) {
			if (ordinal == perimeterOrdinal) {
				return true;
			}
		}
		return false;
	}

}
