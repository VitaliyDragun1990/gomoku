package com.revenat.game.gomoku.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.revenat.game.gomoku.domain.GameSession;
import com.revenat.game.gomoku.domain.Mark;
import com.revenat.game.gomoku.domain.Position;


/**
 * Represents GUI for Gomoku game.
 * 
 * @author Vitaly Dragun
 *
 */
@SuppressWarnings("serial")
public class GameWindow extends JFrame {
	private static final String TITLE = "Gomoku game application";
	private static final int CELL_FONT_SIZE = 35;
	private static final Font DEFAULT_FONT = new Font(Font.SERIF, Font.PLAIN, CELL_FONT_SIZE);
	private static final int CELL_HEIGHT = 45;
	private static final int CELL_WIDTH = 45;
	private static final int TOTAL_ROWS = 15;
	private static final int TOTAL_COLUMNS = 15;
	
	private final GameSession gameSession;
	private final JLabel[] gameTable = new JLabel[TOTAL_ROWS * TOTAL_COLUMNS];
	
	public GameWindow(GameSession gameSession) {
		Objects.requireNonNull(gameSession, "GameSession can not be null.");
		
		this.gameSession = gameSession;
		this.gameSession.addListener(new UIGameEventListener(this));
		
		createGameTable();
		initializeWindow();
		startNewGameSession();
	}
	
	public void displayWindow() {
		setVisible(true);
	}

	private void createGameTable() {
		setLayout(new GridLayout(TOTAL_ROWS, TOTAL_COLUMNS));
		
		for (int index = 0; index < TOTAL_ROWS * TOTAL_COLUMNS; index++) {
			int position = index + 1;
			JLabel tableCell = new JLabel();
			gameTable[index] = tableCell;
			
			tableCell.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
			tableCell.setHorizontalAlignment(SwingConstants.CENTER);
			tableCell.setVerticalAlignment(SwingConstants.CENTER);
			tableCell.setFont(DEFAULT_FONT);
			tableCell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			add(tableCell);
			
			tableCell.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					handlePlayerTurn(position);
				}
				
			});
		}
	}
	
	private void initializeWindow() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int userChoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the game?");
				if (userChoice == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
			
		});
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2 - this.getSize().width/2, dim.height/2 - this.getSize().height/2);
	}
	
	void startNewGameSession() {
		SwingUtilities.invokeLater(() -> {
			clearGameTable();
			gameSession.startNewGame();	
		});
	}
	
	private void clearGameTable() {
		for (int position = 1; position <= TOTAL_ROWS * TOTAL_COLUMNS; position++) {
			renderTableCell(position, Mark.EMPTY);
		}
	}

	void renderTableCell(int position, Mark mark) {
		Color color = mark == Mark.X ? Color.BLUE : Color.BLACK;
		render(position, mark.getValue(), color);
	}
	
	void render(int position, String content, Color color) {
		JLabel tableCell = getTableCellAt(position);
		tableCell.setForeground(color);
		tableCell.setText(content);
	}
	
	private JLabel getTableCellAt(int position) {
		return gameTable[position-1];
	}
	
	private void handlePlayerTurn(int position) {
		gameSession.processPlayerTurn(Position.from(position));
	}
	
}
