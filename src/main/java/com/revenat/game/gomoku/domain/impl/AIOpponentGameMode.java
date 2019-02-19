package com.revenat.game.gomoku.domain.impl;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.GameMode;
import com.revenat.game.gomoku.domain.GameSession;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;

/**
 * Game mode that allows player to play against AI. Player plays with X, AI with O.
 * At the start of the game the right to make a first turn is random.
 * 
 * @author Vitaly Dragun
 *
 */
public class AIOpponentGameMode implements GameMode {
	private static final Mark HUMAN_PLAYER = Mark.X;
	private static final Mark AI_PLAYER = Mark.O;
	
	private final GameSession gameSession;
	private final AIGameOpponent opponent;
	private boolean isHumanTurn = true;

	public AIOpponentGameMode(GameSession gameSession, AIGameOpponent opponent) {
		Objects.requireNonNull(gameSession, "GameSession can not be null.");
		Objects.requireNonNull(opponent, "AIGameOpponent can not be null.");
		
		this.gameSession = gameSession;
		this.opponent = opponent;
	}

	@Override
	public void startGame() {
		randomlyDetermineFirstTurn();

		if (getCurrentPlayer() == AI_PLAYER) {
			processOpponentTurn();
		}

	}
	
	private void processOpponentTurn() {
		Position position = opponent.determineNextTurnPositionFor(getCurrentPlayer());
		if (!position.isValid()) {
			throw new AIOpponentCanNotMakeTurnException("AI opponent can not find position to make turn to. Is it draw?");
		}
		
		gameSession.processPlayerTurn(position);
	}

	@Override
	public Mark getCurrentPlayer() {
		return isHumanTurn ? HUMAN_PLAYER : AI_PLAYER;
	}

	@Override
	public void handleTurnResult(boolean isGameOver) {
		finishPlayerTurn();
		
		if (!isGameOver && getCurrentPlayer() == AI_PLAYER) {
			processOpponentTurn();
		}
	}
	
	private void randomlyDetermineFirstTurn() {
		isHumanTurn = ThreadLocalRandom.current().nextBoolean();
		
	}
	
	private void finishPlayerTurn() {
		isHumanTurn = !isHumanTurn;
	}


}
