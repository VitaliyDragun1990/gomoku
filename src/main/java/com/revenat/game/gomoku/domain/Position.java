package com.revenat.game.gomoku.domain;

import java.util.Objects;

/**
 * This value type represents position on the {@link GameTable} (15x15),
 * both as row-column coordinate (zero-based) and ordinal (from 1 to 225).
 * 
 * @author Vitaly Dragun
 *
 */
public final class Position {
	private static final int CELLS_PER_ROW = 15;
	private static final int ORDINAL_INNER_BOUN = 1;
	private static final int ORDINAL_UPPER_BOUND = 225;
	
	private final int row;
	private final int column;

	private Position(int row, int column) {
		this.row = row;
		this.column = column;
	}


	private Position(int ordinal) {
		row = (ordinal - 1) / CELLS_PER_ROW;
		column = (ordinal - (row * CELLS_PER_ROW)) - 1;
	}
	
	public static Position from(int row, int column) {
		return new Position(row, column);
	}
	
	public static Position from(int ordinal) {
		return new Position(ordinal);
	}
	
	public boolean isValid() {
		return isValid(ordinal());
	}

	public int row() {
		return row;
	}

	public int column() {
		return column;
	}

	public int ordinal() {
		return row * 15 + column + 1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return column == other.column && row == other.row;
	}

	@SuppressWarnings("unused")
	private void checkParams(int row, int column) {
		if (row < 0 || row > 14) {
			throw new IllegalArgumentException("Illegal row number " + row + ". Valid range from 0 to 14 inclusive.");
		}
		if (column < 0 || column > 14) {
			throw new IllegalArgumentException("Illegal row number " + column + ". Valid range from 0 to 14 inclusive.");
		}
	}
	
	@SuppressWarnings("unused")
	private void checkParams(int ord) {
		if (!isValid(ord)) {
			throw new IllegalArgumentException("Illegal ordinal number " + ord + ". Valid range from 1 to 255 inclusive.");
		}
	}
	
	private boolean isValid(int ordn) {
		return ordn >= ORDINAL_INNER_BOUN && ordn <= ORDINAL_UPPER_BOUND;
	}
	
}
