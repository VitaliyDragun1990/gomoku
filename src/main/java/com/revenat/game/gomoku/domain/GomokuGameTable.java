package com.revenat.game.gomoku.domain;

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
		return getCellAt(position) == Mark.EMPTY;
	}
	
	@Override
	public boolean isCellMarked(Position position, Mark mark) {
		return getCellAt(position) == mark;
	}
	
	private Mark getCellAt(Position position) {
		return gameTable[position.row()][position.column()];
	}
	
	@Override
	public void markCellAt(Position position, Mark mark) {
		gameTable[position.row()][position.column()] = mark;
	}
	
	@Override
	public Mark getCellMark(Position position) {
		return getCellAt(position);
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

}
