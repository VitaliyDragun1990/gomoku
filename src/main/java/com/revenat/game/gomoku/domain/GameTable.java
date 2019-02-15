package com.revenat.game.gomoku.domain;

/**
 * Represents game table role for abstract game that has grid
 * with rows and columns, and each cell on that grid can be marked
 * with specific {@link Mark}.
 * 
 * @author Vitaly Dragun
 *
 */
public interface GameTable {

	/**
	 * Clears game grid
	 */
	public void clear();
	
	/**
	 * Checks whether a grid cell at the specified position is empty.
	 */
	public boolean isCellEmpty(Position position);
	
	/**
	 * Checks whether a grid cell at the give position is marked with
	 * specified mark.
	 */
	public boolean isCellMarked(Position position, Mark mark);
	
	/**
	 * Marks grid cell at the specified position with specified mark.
	 */
	public void markCellAt(Position position, Mark mark);
	
	/**
	 * Returns mark the given game cell is marked with.
	 */
	public Mark getCellMark(Position position);

	/**
	 * Returns total number of rows in this game table
	 */
	public int getTotalRows();

	/**
	 * Returns total number of columns in this game table
	 */
	public int getTotalColumns();

	/**
	 * Returns total number of cells in this game table
	 * @return
	 */
	public int getTotalCells();
}
