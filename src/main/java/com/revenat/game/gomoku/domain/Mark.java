package com.revenat.game.gomoku.domain;

/**
 * This enum represents a player's designation mark
 * which can be used on {@link GameTable}.
 * 
 * @author Vitaly Dragun
 *
 */
public enum Mark {
	X {
		@Override
		public String toString() {
			return " X ";
		}
		@Override
		public Mark getOpponent() {
			return O;
		}
	},
	O {
		@Override
		public String toString() {
			return " O ";
		}
		@Override
		public Mark getOpponent() {
			return X;
		}
	},
	EMPTY {
		@Override
		public String toString() {
			return "   ";
		}
		@Override
		public Mark getOpponent() {
			throw new UnsupportedOperationException("Empty mark does not have an opponent");
		}
	};
	
	@Override
	public abstract String toString();
	
	public abstract Mark getOpponent();
}
