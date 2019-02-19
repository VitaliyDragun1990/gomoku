package com.revenat.game.gomoku;

import javax.swing.JOptionPane;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.GameArbiter;
import com.revenat.game.gomoku.domain.GameMode;
import com.revenat.game.gomoku.domain.GameSession;
import com.revenat.game.gomoku.domain.GameTable;
import com.revenat.game.gomoku.domain.impl.AIOpponentGameMode;
import com.revenat.game.gomoku.domain.impl.GomokuGameSession;
import com.revenat.game.gomoku.domain.impl.GomokuGameTable;
import com.revenat.game.gomoku.domain.impl.ImprovedGomokuAIGameOpponent;
import com.revenat.game.gomoku.domain.impl.ImprovedGomokuGameArbiter;
import com.revenat.game.gomoku.domain.impl.PlayerOpponentGameMode;
import com.revenat.game.gomoku.ui.GameWindow;



/**
 * Main starting point for Gomoku game.
 * 
 * @author Vitaly Dragun
 *
 */
public class Main {
	private static final String GAME_MODE_MESSAGE = "Please select game mode:";
	private static final String GAME_MODE_TITLE = "Game mode selection";
	private static final String MODE_AGAINST_PLAYER = "Play against another player";
	private static final String MODE_AGAINST_COMPUTER = "Play against computer";
	
	private GameSession gameSession;
	private GameTable gameTable;
	
	public Main() {
		gameSession = buildGameSession();
	}
	
	private GameSession buildGameSession() {
		gameTable = new GomokuGameTable();
//		GameArbiter arbiter = new DefaultGomokuGameArbiter(gameTable);
		GameArbiter arbiter = new ImprovedGomokuGameArbiter(gameTable);
		return new GomokuGameSession(gameTable, arbiter);
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

	private GameMode againstPlayer() {
		return new PlayerOpponentGameMode();
	}
	
	private GameMode againstAI() {
		return new AIOpponentGameMode(gameSession, getAIOpponent());
	}
	
	private AIGameOpponent getAIOpponent() {
//		return new DefaultGomokuAIGameOpponent(gameTable);
		return new ImprovedGomokuAIGameOpponent(gameTable);
	}

	public static void main(String[] args) {
		Main main = new Main();
		
		String userChoise = getUserChoise();
		
		if (MODE_AGAINST_PLAYER.equals(userChoise)) {
			main.startGameAgainstPlayer();
		} else if (MODE_AGAINST_COMPUTER.equals(userChoise)) {
			main.startGameAgainstAI();
		}
	}

	/*private static DifficultyLevel getDifficultyLevel() {
		String level = (String) JOptionPane.showInputDialog(null, DIFFICULTY_LEVEL_MESSAGE, DIFFICULTY_LEVEL_TITLE,
				JOptionPane.QUESTION_MESSAGE, null, new String[]{LEVEL_HARD, LEVEL_EASY},
				LEVEL_EASY);
		return DifficultyLevel.valueOf(level);
	}*/

	private static String getUserChoise() {
		return (String) JOptionPane.showInputDialog(null, GAME_MODE_MESSAGE, GAME_MODE_TITLE,
				JOptionPane.QUESTION_MESSAGE, null, new String[]{MODE_AGAINST_COMPUTER, MODE_AGAINST_PLAYER},
				MODE_AGAINST_COMPUTER);
	}
}
