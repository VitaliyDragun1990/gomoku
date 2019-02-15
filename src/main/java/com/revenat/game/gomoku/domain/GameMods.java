package com.revenat.game.gomoku.domain;

/**
 * Factory class for different {@link GameMode}s available for Gomoku game
 * session.
 * 
 * @author Vitaly Dragun
 *
 */
public final class GameMods {

	private GameMods() {
	}

	/**
	 * Return {@link GameMode} implementation for playing against another player.
	 */
	public static GameMode playerOpponent() {
		return new PlayerOpponentGameMode();
	}

	/**
	 * Return {@link GameMode} implementation for playing against AI computer
	 * opponent.
	 * 
	 * @param gameSession reference to specific {@link GameSession} for which given
	 *                    game mode will be applied.
	 * @param opponent    realization of the {@link AIGameOpponent} which contains
	 *                    an algorithm to help AI opponent to play against player.
	 * @return
	 */
	public static GameMode computerOpponent(GameSession gameSession, AIGameOpponent opponent) {
		return new AIOpponentGameMode(gameSession, opponent);
	}
}
