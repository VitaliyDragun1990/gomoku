package com.revenat.game.gomoku.domain;

import java.util.Arrays;

/**
 * Represents a role responsible for checking current status of the
 * game on the game table and determine whether there is a winner,
 *  a draw or the game can be continued.
 *  
 * @author Vitaly Dragun
 *
 */
public interface GameArbiter {
	
	CheckResult checkForGameOver();

	/**
	 * Represent result returned by {@link GameArbiter} after checking the game state.
	 * @author Vitaly Dragun
	 *
	 */
	static class CheckResult {
		private final Position[] winningCombination;
		private final boolean isDraw;
		
		private CheckResult(Position[] winningCombination, boolean isDraw) {
			this.winningCombination = Arrays.copyOf(winningCombination, winningCombination.length);
			this.isDraw = isDraw;
		}
		
		public boolean isWinner() {
			return winningCombination.length > 0;
		}
		
		public boolean isDraw() {
			return isDraw;
		}
		
		public Position[] getWinningCombination() {
			if (isWinner()) {
				return winningCombination;
			}
			throw new IllegalStateException("You can not get the winning combination becouse there is no winner.");
		}
		
		public static CheckResult winner(Position[] winningCombination) {
			return new CheckResult(winningCombination, false);
		}
		
		public static CheckResult draw() {
			return new CheckResult(new Position[0], true);
		}
		
		public static CheckResult keepOnPlaying() {
			return new CheckResult(new Position[0], false);
		}
	}
}
