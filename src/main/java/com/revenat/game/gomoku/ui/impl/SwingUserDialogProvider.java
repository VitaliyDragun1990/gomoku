package com.revenat.game.gomoku.ui.impl;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

import javax.swing.JOptionPane;

import com.revenat.game.gomoku.ui.UserDialogProvider;

/**
 * Implementation of the {@link UserDialogProvider} using java Swing framework
 * capabilities.
 * 
 * @author Vitaly Dragun
 *
 */
public class SwingUserDialogProvider implements UserDialogProvider {

	@Override
	public void showMessageDialog(String dialogMessage, String dialogTitle) {
		JOptionPane.showMessageDialog(null, dialogMessage, dialogTitle, INFORMATION_MESSAGE);

	}

	@Override
	public boolean showConfirmationDialog(String dialogMessage, String dialogTitle) {
		int userChoice = JOptionPane.showConfirmDialog(null, dialogMessage, dialogTitle, YES_NO_OPTION,
				INFORMATION_MESSAGE);
		return userChoice == YES_OPTION;
	}

	@Override
	public String showInputDialog(String dialogMessage, String dialogTitle, String... selectionValues) {
		return (String) JOptionPane.showInputDialog(null, dialogMessage, dialogTitle, JOptionPane.QUESTION_MESSAGE,
				null, selectionValues, selectionValues[0]);
	}

}
