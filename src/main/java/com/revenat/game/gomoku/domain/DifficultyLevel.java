package com.revenat.game.gomoku.domain;

/**
 * Represents different difficulty levels that can be applied
 * to game when playing against AI opponent.
 * 
 * @author Vitaly Dragun
 *
 */
public enum DifficultyLevel {

	HARD(3),
	EASY(4);
	
	private int level;
	
	DifficultyLevel(int level) {
		this.level = level;
	}
	
	public int level() {
		return level;
	}
}
