package com.revenat.game.gomoku.domain;

import java.util.concurrent.ThreadLocalRandom;

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
