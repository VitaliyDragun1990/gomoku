package com.revenat.game.gomoku.domain.impl;

import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.game.gomoku.domain.GameArbiter;
import com.revenat.game.gomoku.domain.GameEventListener;
import com.revenat.game.gomoku.domain.GameMode;
import com.revenat.game.gomoku.domain.GameSession;
import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;
import com.revenat.game.gomoku.domain.GameArbiter.CheckResult;
import com.revenat.game.gomoku.infra.Announcer;

/**
 * Implementation of {@link GameSession}, responsible for managing Gomoku game
 * session.
 * 
 * @author Vitaly Dragun
 *
 */
public class GomokuGameSession implements GameSession {
	private static final Logger LOG = LoggerFactory.getLogger(GomokuGameSession.class);
	
	private final GameTable gameTable;
	private final GameArbiter arbiter;
	private final Announcer<GameEventListener> listeners = Announcer.to(GameEventListener.class);
	
	/**
	 * Default game mode - against another player
	 */
	private GameMode gameMode = GameMods.playerOpponent();

	public GomokuGameSession(GameTable gameTable, GameArbiter arbiter) {
		requireNonNull(gameTable, "GameTable can not be null.");
		requireNonNull(arbiter, "GameArbiter can not be null.");
		
		this.gameTable = gameTable;
		this.arbiter = arbiter;
	}
	
	@Override
	public void addListener(GameEventListener listener) {
		requireNonNull(listener, "Listener can not be null");
		listeners.addListener(listener);
	}
	
	@Override
	public void setGameMode(GameMode gameMode) {
		requireNonNull(gameMode, "GameMode can not be null");
		this.gameMode = gameMode;
	}
	
	@Override
	public void startNewGame() {
		clearGameTable();
		gameMode.startGame();
		LOG.info("Start new game session.");
	}
	
	@Override
	public void processPlayerTurn(Position position) {
		requireNonNull(position, "Turn position can not be null.");
		LOG.debug("Player '{}' chooses position {} to make turn to", gameMode.getCurrentPlayer(), position);
		
		if (isCellOccupied(position)) {
			LOG.warn("Player '{}' chose already occupied position {} to make turn to.",
					gameMode.getCurrentPlayer(), position);
			listeners.announce().invalidTurnPosition(position);
			return;
		}
		
		boolean gameOver = handleTurnTo(position);
		gameMode.handleTurnResult(gameOver);
	}	
	
	private boolean handleTurnTo(Position position) {
		Mark currentPlayer = gameMode.getCurrentPlayer();
		takeTurn(position, currentPlayer);
		listeners.announce().turnIsMade(position, currentPlayer);
		LOG.info("Player '{}' made turn to position {}", currentPlayer, position);
		
		CheckResult checkResult = arbiter.checkForGameOver();
		if (checkResult.isWinner()) {
			Position[] winningCombination = checkResult.getWinningCombination();
			LOG.info("Game over. Winner: '{}' with winning combination {}", currentPlayer, winningCombination);
			listeners.announce().winnerIsFound(currentPlayer, winningCombination);
			return true;
		} else if (checkResult.isDraw()) {
			LOG.info("Game over. Is is a draw.");
			listeners.announce().draw();
			return true;
		}
		
		return false;
	}

	private void takeTurn(Position position, Mark playerMark) {
		gameTable.markCellAt(position, playerMark);
	}

	private void clearGameTable() {
		gameTable.clear();
	}

	private boolean isCellOccupied(Position position) {
		return !gameTable.isCellEmpty(position);
	}

}
