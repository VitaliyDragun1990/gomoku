package com.revenat.game.gomoku;

import javax.swing.JOptionPane;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.AIOpponentGameMode;
import com.revenat.game.gomoku.domain.GameArbiter;
import com.revenat.game.gomoku.domain.GameMode;
import com.revenat.game.gomoku.domain.GameSession;
import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.GomokuAIGameOpponent;
import com.revenat.game.gomoku.domain.GomokuGameArbiter;
import com.revenat.game.gomoku.domain.GomokuGameSession;
import com.revenat.game.gomoku.domain.GomokuGameTable;
import com.revenat.game.gomoku.domain.PlayerOpponentGameMode;
import com.revenat.game.gomoku.ui.GameWindow;



/**
 * Main starting point for Gomoku game.
 * 
 * @author Vitaly Dragun
 *
 */
public class Main {
	private static final String TITLE = "Game mode selection";
	private static final String MESSAGE = "Please select game mode:";
	private static final String AGAINST_PLAYER = "Play against another player";
	private static final String AGAINST_COMPUTER = "Play against computer";
	
	private GameSession gameSession;
	private GameTable gameTable;
	
	public Main() {
		gameSession = buildGameSession();
	}
	
	private void startGameAgainstPlayer() {
		startGame(againstPlayer());
	}
	
	private void startGameAgainstAI() {
		startGame(againstAI());
	}
	
	public void startGame(GameMode gameMode) {
		gameSession.setGameMode(gameMode);
		GameWindow window = new GameWindow(gameSession);
		window.displayWindow();
	}
	
	private GameSession buildGameSession() {
		gameTable = new GomokuGameTable();
		GameArbiter arbiter = new GomokuGameArbiter(gameTable);
		return new GomokuGameSession(gameTable, arbiter);
	}
	
	private GameMode againstPlayer() {
		return new PlayerOpponentGameMode();
	}
	
	private GameMode againstAI() {
		return new AIOpponentGameMode(gameSession, getAIOpponent());
	}
	
	private AIGameOpponent getAIOpponent() {
		return new GomokuAIGameOpponent(gameTable);
	}

	public static void main(String[] args) {
		Main main = new Main();
		
		String userChoise = getUserChoise();
		
		if (AGAINST_PLAYER.equals(userChoise)) {
			main.startGameAgainstPlayer();
		} else if (AGAINST_COMPUTER.equals(userChoise)) {
			main.startGameAgainstAI();
		}
	}

	private static String getUserChoise() {
		return (String) JOptionPane.showInputDialog(null, MESSAGE, TITLE,
				JOptionPane.QUESTION_MESSAGE, null, new String[]{AGAINST_COMPUTER, AGAINST_PLAYER},
				AGAINST_COMPUTER);
	}
}
