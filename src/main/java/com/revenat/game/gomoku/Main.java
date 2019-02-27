package com.revenat.game.gomoku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.game.gomoku.domain.AIGameOpponent;
import com.revenat.game.gomoku.domain.GameArbiter;
import com.revenat.game.gomoku.domain.GameEventListener;
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
import com.revenat.game.gomoku.ui.UserDialogProvider;
import com.revenat.game.gomoku.ui.impl.SwingGameWindow;
import com.revenat.game.gomoku.ui.impl.SwingUserDialogProvider;
import com.revenat.game.gomoku.ui.impl.UIGameEventListener;



/**
 * Main starting point for Gomoku game.
 * 
 * @author Vitaly Dragun
 *
 */
public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
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
		LOG.info("Starting new game agains another player");
		startGame(againstPlayer());
	}
	
	private void startGameAgainstAI() {
		LOG.info("Starting new game agains AI opponent");
		startGame(againstAI());
	}
	
	public void startGame(GameMode gameMode) {
		gameSession.setGameMode(gameMode);
		GameWindow window = new SwingGameWindow(gameSession);
		UserDialogProvider dialogProvider = new SwingUserDialogProvider();
		GameEventListener gameListener = new UIGameEventListener(window, dialogProvider);
		gameSession.addListener(gameListener);
		window.startNewGame();
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
		
		String userChoise = new SwingUserDialogProvider()
				.showInputDialog(GAME_MODE_MESSAGE, GAME_MODE_TITLE, MODE_AGAINST_COMPUTER, MODE_AGAINST_PLAYER);
		
		if (MODE_AGAINST_PLAYER.equals(userChoise)) {
			main.startGameAgainstPlayer();
		} else if (MODE_AGAINST_COMPUTER.equals(userChoise)) {
			main.startGameAgainstAI();
		}
	}
}
