package com.revenat.game.gomoku.domain;

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
	private final GameTable gameTable;
	private final GameArbiter arbiter;
	private final Announcer<GameEventListener> listeners = Announcer.to(GameEventListener.class);
	
	/**
	 * Default game mode - against another player
	 */
	private GameMode gameMode = GameMods.playerOpponent();

	public GomokuGameSession(GameTable gameTable, GameArbiter arbiter) {
		this.gameTable = gameTable;
		this.arbiter = arbiter;
	}
	
	@Override
	public void addListener(GameEventListener listener) {
		listeners.addListener(listener);
	}
	
	@Override
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	@Override
	public void startNewGame() {
		clearGameTable();
		gameMode.startGame();
	}
	
	@Override
	public void processPlayerTurn(Position position) {
		if (isCellOccupied(position)) {
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
		
		CheckResult checkResult = arbiter.checkForGameOver();
		if (checkResult.isWinner()) {
			listeners.announce().winnerIsFound(currentPlayer, checkResult.getWinningCombination());
			return true;
		} else if (checkResult.isDraw()) {
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
