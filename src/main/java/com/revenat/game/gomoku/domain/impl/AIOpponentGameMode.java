package com.revenat.game.gomoku.domain.impl;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.GameMode;
import com.revenat.game.gomoku.domain.GameSession;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;
import com.revenat.game.gomoku.domain.exception.AIOpponentCanNotMakeTurnException;

/**
 * Game mode that allows player to play against AI. Player plays with X, AI with O.
 * At the start of the game the right to make a first turn is random.
 * 
 * @author Vitaly Dragun
 *
 */
public class AIOpponentGameMode implements GameMode {
	private static final Logger LOG = LoggerFactory.getLogger(AIOpponentGameMode.class);
	
	private static final Mark HUMAN_PLAYER = Mark.X;
	private static final Mark AI_PLAYER = Mark.O;
	
	private final GameSession gameSession;
	private final AIGameOpponent opponent;
	private boolean isHumanTurn = true;

	public AIOpponentGameMode(GameSession gameSession, AIGameOpponent opponent) {
		requireNonNull(gameSession, "GameSession can not be null.");
		requireNonNull(opponent, "AIGameOpponent can not be null.");
		
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
		LOG.debug("AI opponent '{}' tries to find a free position to move to.", getCurrentPlayer());
		Position position = opponent.determineNextTurnPositionFor(getCurrentPlayer());
		if (!position.isValid()) {
			LOG.error("AI opponent '{}' can not find a free position to make a move to.", getCurrentPlayer());
			throw new AIOpponentCanNotMakeTurnException("No free position to move to. Is it draw?");
		}
		
		LOG.debug("AI opponent '{}' choose position {} to make it's move to.", getCurrentPlayer(), position);
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

	@Override
	public String toString() {
		return "AIOpponentGameMode";
	}

}
