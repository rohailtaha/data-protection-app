package dataProtection;

import javax.swing.JOptionPane;

public class Messages 
{
	static void showMultipleFilesError()
	{
		String message = String.format("%s%n%s","This directory contains multiple files. Enable \"multiple files selection\"", "from settings to perform the task on multiple files.");
		JOptionPane.showMessageDialog(Main.gui, message, "An error occured", JOptionPane.ERROR_MESSAGE);
	}
	
	static void showEmptyDirectoryError()
	{
		String message = String.format("%s%n%s","No files found were found inside the selected", "directory/directories and subdirectories.");
		JOptionPane.showMessageDialog(Main.gui, message, "No files found", JOptionPane.ERROR_MESSAGE);
	}
	
	static void showNoPasswordChoosenError()
	{
		String message = String.format("%s","No password choosen. Must specify a password.");
		JOptionPane.showMessageDialog(Main.gui, message, "No password choosen", JOptionPane.ERROR_MESSAGE);
	}
	
	static void showInvalidPasswordError()
	{
		String message = String.format("%s%n%s%n%s","The password is invalid. Make sure the password is at least 4", 
				"characters long and contains only alphanumeric characters", "with no spaces. Please try again.");
		JOptionPane.showMessageDialog(Main.gui, message, "Invalid password", JOptionPane.ERROR_MESSAGE); 
	}
	
	static void showTooManyFilesError()
	{
		String message = String.format("%s%n%s","The number of files exceed the  maximum allowed limit (1000).", "Make sure that maximum number of files are not above 1000.");
		JOptionPane.showMessageDialog(Main.gui, message, "Limit exceeded", JOptionPane.ERROR_MESSAGE);
	}
	
	static void showCantEncryptDecryptError()
	{
		String message = String.format("%s%n%s%n%s", "The operation can not be completed successfully. The output files", 
				"may be corrupted. Before decrypting, please make sure that appropriate", "files are selected for the operation.");
		JOptionPane.showMessageDialog(Main.gui, message, "An error occurred", JOptionPane.ERROR_MESSAGE);
	}
	
	static void showGeneralError()
	{
		JOptionPane.showMessageDialog(Main.gui, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	static int showFilesDeletionWarningForEncryption()
	{
		String message = String.format("%s%n%s%n%s", "All the original files will be deleted! The \"Delete original "
				+ "files after encryption\"", "option is enabled in settings.The files will be permanently deleted from the",
				"system. Do you want to delete the " + "original files?");
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(Main.gui, message, "Warning", JOptionPane.YES_NO_OPTION);
		return choice;
	}	

	static int showFilesDeletionWarningForDecryption()
	{
		String message = String.format("%s%n%s%n%s", "The \"Delete encrypted files after "
				+ "decryption\" option is enabled", "in settings. The files will be permanently deleted from the", 
				"system. Do you want to delete the encrypted files?");
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(Main.gui, message, "Warning", JOptionPane.YES_NO_OPTION);
		return choice;
	}	
	
	static int showFilesDeletionWarningForPasswordProtection()
	{
		String message = String.format("%s%n%s%n%s%n%s", "All the original files will be deleted! The \"Delete original "
				+ "files after", "password protection\" option is enabled in settings. The files will", "be permanently deleted from" + 
				"the system. Do you want to delete", "the original files? ");
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(Main.gui, message, "Warning", JOptionPane.YES_NO_OPTION);
		return choice;
	}
	
	static void showTaskCompletionMessage(String taskName)
	{
		if(taskName.equals("Encryption"))
			JOptionPane.showMessageDialog(Main.gui, taskName + " of selected file/s completed. Warning! Opening an\n encrypted file may cause the file to show unusual behaviour.");
		else
		JOptionPane.showMessageDialog(Main.gui, taskName + " of selected file/s completed");
	}
	
	static int askTaskConfirmation(String taskName)
	{
		int choice = JOptionPane.showConfirmDialog(Main.gui, "Confirm " + taskName + " of file/s?", "", JOptionPane.YES_NO_OPTION);
		return choice;
	}
	
	static int askDecryptionConfirmation()
	{
		String message = String.format("%s%n%s", "Confirm decryption of files? Before confirming, make sure that",
				"only encrypted files are selected for decryption.");
		int choice = JOptionPane.showConfirmDialog(Main.gui, message, "", JOptionPane.YES_NO_OPTION);
		return choice;
	}
	
	static void showTaskCancelledMessage(String taskName)
	{
		JOptionPane.showMessageDialog(Main.gui, taskName + " was cancelled");
	}	
	
	static void showSettingsSavedMessage()
	{
		JOptionPane.showMessageDialog(Main.gui, "            			Changes saved");
	}

	static void showDefaultSettingsSavedMessage()
	{
		JOptionPane.showMessageDialog(Main.gui, "        Default settings saved");
	}

}
