package com.revenat.game.gomoku.domain;


/**
 * Game mode that allows two player to play against
 * each other taking turns.
 * 
 * @author Vitaly Dragun
 *
 */
public class PlayerOpponentGameMode implements GameMode {
	private static final Mark PLAYER_X = Mark.X;
	private static final Mark PLAYER_Y = Mark.O;

	private boolean xTurn = true;

	public PlayerOpponentGameMode() {
		// empty for now
	}

	@Override
	public void startGame() {
		xMoveFirst();
	}

	private void xMoveFirst() {
		xTurn = true;
		
	}

	@Override
	public Mark getCurrentPlayer() {
		return xTurn ? PLAYER_X : PLAYER_Y;
	}

	@Override
	public void handleTurnResult(boolean isGameOver) {
		finishPlayerTurn();
	}
	
	private void finishPlayerTurn() {
		xTurn = !xTurn;
	}


}
