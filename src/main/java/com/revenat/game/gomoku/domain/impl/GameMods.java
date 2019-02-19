package com.revenat.game.gomoku.domain.impl;

import java.util.Objects;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.GameMode;
import com.revenat.game.gomoku.domain.GameSession;

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
		Objects.requireNonNull(gameSession, "GameSession can not be null.");
		Objects.requireNonNull(opponent, "AIGameOpponent can not be null.");
		
		return new AIOpponentGameMode(gameSession, opponent);
	}
}
