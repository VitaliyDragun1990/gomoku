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
		public String getValue() {
			return " X ";
		}
		@Override
		public Mark getOpponent() {
			return O;
		}
	},
	O {
		@Override
		public String getValue() {
			return " O ";
		}
		@Override
		public Mark getOpponent() {
			return X;
		}
	},
	EMPTY {
		@Override
		public String getValue() {
			return "   ";
		}
		@Override
		public Mark getOpponent() {
			throw new UnsupportedOperationException("Empty mark does not have an opponent");
		}
	};
	
	@Override
	public String toString() {
		return getValue();
	}
	
	public abstract String getValue();
	
	public abstract Mark getOpponent();
}
