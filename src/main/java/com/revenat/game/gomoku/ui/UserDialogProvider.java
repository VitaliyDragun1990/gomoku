package com.revenat.game.gomoku.ui;

/**
 * Abstract provider that presents a user some sort of a dialog window.
 * 
 * @author Vitaly Dragun
 *
 */
public interface UserDialogProvider {

	/**
	 * Shows message dialog to user.
	 * 
	 * @param dialogMessage message that the dialog convey.
	 * @param dialogTitle   title of the presented dialog
	 */
	void showMessageDialog(String dialogMessage, String dialogTitle);

	/**
	 * Shows dialog with Yes/No options
	 * 
	 * @param dialogMessage message that the dialog convey.
	 * @param dialogTitle   title of the presented dialog
	 * @return true if the user chose {@code Yes} option, false otherwise.
	 */
	boolean showConfirmationDialog(String dialogMessage, String dialogTitle);
	
	/**
	 * Shows input dialog with multiple selection values.
	 * 
	 * @param dialogMessage message that the dialog convey.
	 * @param dialogTitle   title of the presented dialog
	 * @param selectionValues values  user have to select among
	 * @return selected value.
	 */
	String showInputDialog(String dialogMessage, String dialogTitle, String... selectionValues);
}
