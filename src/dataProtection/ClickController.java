package dataProtection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

public class ClickController implements ActionListener, ItemListener
{
	static final Settings settings = new Settings();
	private Path[] selectedFilesPaths = new Path[1000];
	private Path outputFolderPath;
	private Path[] outputFilesPaths = new Path[1000];
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		final FilesSelection filesSelection = new FilesSelection();
		boolean fileDeletionFlag = false;
		
		try {
			if(e.getActionCommand().equals("encrypt")) {

				getPathsForFilesAndDirectory("encryption", "encrypted", filesSelection);
				if(selectedFilesPaths == null || outputFolderPath == null)
					return;
				
				if(settings.isEncryptionConfirmationOn()) {        // encryption confirmation block
					if(!wasTaskConfirmed("encryption"))
						return;
				}
				
				if(settings.isDelModeAfterEncryptionOn()) {             // deletion warning block
					int choice = Messages.showFilesDeletionWarningForEncryption();
					if(choice == -1) {
						Messages.showTaskCancelledMessage("Encryption");
						return;
					}
					if(choice == JOptionPane.YES_OPTION)
						fileDeletionFlag = true;
					else if(choice == JOptionPane.NO_OPTION)
						fileDeletionFlag = false;
				} 
				initiateTask("encryption", selectedFilesPaths, outputFilesPaths, outputFolderPath, fileDeletionFlag);
			}
		
			if(e.getActionCommand().equals("decrypt")) {
				
				getPathsForFilesAndDirectory("decryption", "decrypted", filesSelection);
				if(selectedFilesPaths == null || outputFolderPath == null)
					return;
				
				if(settings.isDecryptionConfirmationOn()) {  // decryption confirmation block
						if(!wasTaskConfirmed("decryption"))
							return;
				}
				
				if(settings.isDelModeAfterDecryptionOn()) {       // deletion warning block
					int choice = Messages.showFilesDeletionWarningForDecryption();
					if(choice == -1) {
						Messages.showTaskCancelledMessage("Decryption");
						return;
					}
					if(choice == JOptionPane.YES_OPTION)
						fileDeletionFlag = true;
					else if(choice == JOptionPane.NO_OPTION)
						fileDeletionFlag = false;
				} 
				
				initiateTask("decryption", selectedFilesPaths, outputFilesPaths, outputFolderPath, fileDeletionFlag);
			}	
			
			if(e.getActionCommand().equals("pp")) {
				
				getPathsForFilesAndDirectory("password protection", "protected", filesSelection);
				if(selectedFilesPaths == null || outputFolderPath == null)
					return;
				
				if(!wasPasswordChoosen())
					return;
				
				if(settings.isPasswordProtectionConfirmationOn()) {  // protection confirmation block 
						if(!wasTaskConfirmed("password protection"))
							return;
					}
				
				if(settings.isDelModeAfterPPOn()) {    // deletion warning block
					int choice = Messages.showFilesDeletionWarningForPasswordProtection();
					if(choice == -1) {
						Messages.showTaskCancelledMessage("Password protection");
						return;
					}
					if(choice == JOptionPane.YES_OPTION)
						fileDeletionFlag = true;
					else if(choice == JOptionPane.NO_OPTION)
						fileDeletionFlag = false;
				} 
				
				initiateTask("password protection", selectedFilesPaths, outputFilesPaths, outputFolderPath, fileDeletionFlag);
			}
			
			if(e.getActionCommand().equals("settings")) {
				Main.gui.initialWinContainer.setVisible(false);
				Main.gui.setSettingsWindow();
				settings.setSavedSettings();
				Main.gui.saveSettingsBtn.setEnabled(false);
				Main.gui.settingsWinContainer.setVisible(true);
			}
			
			if(e.getActionCommand().equals("apply")) {
				settings.saveChangedSettings();
				Messages.showSettingsSavedMessage();
				Main.gui.saveSettingsBtn.setEnabled(false);
			}
			
			if(e.getActionCommand().equals("default")) {
				settings.saveDefaultSettings();
				settings.setSavedSettings();
				Main.gui.saveSettingsBtn.setEnabled(false);
				Messages.showDefaultSettingsSavedMessage();
			}
			
			if(e.getActionCommand().equals("back")) {
				Main.gui.settingsWinContainer.setVisible(false);
				Main.gui.setInitialWindow();
				Main.gui.initialWinContainer.setVisible(true);
			}
		}

		catch (NullPointerException exception) {
			Messages.showGeneralError();
			return;
		}
		catch (ClassNotFoundException exception) {
			Messages.showGeneralError();
			return;
		}
		catch (IOException exception) {
			Main.gui.progressFrame.setVisible(false);
			Messages.showGeneralError();
			return;
		}
		catch(ArrayIndexOutOfBoundsException exception) {
			Messages.showTooManyFilesError();
			return;
		}
		
	}
	
	private void getPathsForFilesAndDirectory(String taskName, String taskNamePast, FilesSelection filesSelection) throws IOException, ClassNotFoundException
	{
		filesSelection.setFileChooserParameters("Choose file/s to be " + taskNamePast, taskName, JFileChooser.FILES_AND_DIRECTORIES);
		selectedFilesPaths = filesSelection.chooseFiles();
		if(selectedFilesPaths == null) 
			return;
		filesSelection.setFileChooserParameters("Choose " + taskNamePast + " file/s destination", JFileChooser.DIRECTORIES_ONLY);
		outputFolderPath = filesSelection.chooseFilesDestination();
		if(outputFolderPath == null) 
			return;
	}
	
	private void initiateTask(String taskName, Path[] selectedFilesPaths, Path[] outputFilesPaths, 
			Path outputFolderPath, boolean fileDeletionFlag) throws IOException
	{
		Runnable task;
		if(taskName.equals("encryption"))  
			task = new EncryptionDecryptionTask(Cipher.ENCRYPT_MODE, selectedFilesPaths, outputFilesPaths, outputFolderPath, fileDeletionFlag);
		else if(taskName.equals("decryption")) 
			task = new EncryptionDecryptionTask(Cipher.DECRYPT_MODE, selectedFilesPaths, outputFilesPaths, outputFolderPath, fileDeletionFlag);
		else 
			task = new MakeZipTask(selectedFilesPaths, outputFolderPath, fileDeletionFlag);
		
		Main.gui.makeProgressFrame(0, FileOperations.getFilesSize(selectedFilesPaths));
		UpdateProgressTask updateProgressTask = new UpdateProgressTask(0, FileOperations.getFilesSize(selectedFilesPaths), taskName);
		ExecutorService executorService = Executors.newCachedThreadPool();
		Main.gui.disableMainActionButtons();
		executorService.execute(task);
		executorService.execute(updateProgressTask);
		executorService.shutdown();
		FileOperations.resetAttributes();
	}
	
	private boolean wasTaskConfirmed(String taskName)
	{
		int choice = -1;
		choice = Messages.askTaskConfirmation(taskName);
		if(choice == JOptionPane.NO_OPTION || choice == -1 ) {
			// Make the first letter of 'taskName' capitalized. (e.g. change 'encryption' to Encryption )
			String _taskName = taskName.substring(0, 1).toUpperCase() + taskName.substring(1);  
			Messages.showTaskCancelledMessage(_taskName);
			return false;
		}
		return true;
	}
	
	private boolean wasPasswordChoosen()
	{
		String password;
		do {
			password = JOptionPane.showInputDialog(Main.gui, "Choose a password");
			if(!isPasswordValid(password)) {
				if(password == null) {
					Messages.showTaskCancelledMessage("Password protection");
					return false;
				}
				else
					Messages.showInvalidPasswordError();
			}
		} while(!isPasswordValid(password));
		MakeZipTask.setPassword(password);
		return true;
	}
	
	private boolean isPasswordValid(String password)
	{
		if(password == null || password.length()<4 || password.contains(" ") || password.isEmpty())
		return false;
		
		int alphaNumericCharCount = 0;
		for(int i=0; i<password.length();i++) {
			if(isChar(password.toCharArray()[i]))
				++alphaNumericCharCount;
		}
		if(alphaNumericCharCount == password.length())
			return true;
		return false;
	}

	private boolean isChar(char ch)
	{
		return ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch<='9')); 
	}
	
	public void itemStateChanged(ItemEvent e) 
	{
		try {
			Main.gui.saveSettingsBtn.setEnabled(true);
			
			if(e.getSource() instanceof JRadioButton) {
				JRadioButton button = (JRadioButton) e.getSource();
			
				if(button.getText().equals("Delete original files after encryption")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.setDelModeAfterEncryption(true);
					
					if(e.getStateChange() == ItemEvent.DESELECTED) 
						settings.setDelModeAfterEncryption(false);
				}
				
				if(button.getText().equals("Allow only single file to be encrypted at a time")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.setMultiSelectionOnForEncryption(false);
					
					if(e.getStateChange() == ItemEvent.DESELECTED) 
						settings.setMultiSelectionOnForEncryption(true);
				}
				
				if(button.getText().equals("Delete encrypted files after decryption")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.setDelModeAfterDecryption(true);
					
					if(e.getStateChange() == ItemEvent.DESELECTED) 
						settings.setDelModeAfterDecryption(false );
				}
				
				if(button.getText().equals("Allow only single file to be decrypted at a time")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.setMultiSelectionOnForDecryption(false);
					
					if(e.getStateChange() == ItemEvent.DESELECTED) 
						settings.setMultiSelectionOnForDecryption(true);
				}
				
				if(button.getText().equals("Delete original files after password protection")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.setDelModeAfterPP(true);
					
					if(e.getStateChange() == ItemEvent.DESELECTED) 
						settings.setDelModeAfterPP(false);
				}
				
				if(button.getText().equals("Allow only single file to be password protected at a time")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.setMultiSelectionOnForPasswordProtection(false);
					
					if(e.getStateChange() == ItemEvent.DESELECTED) 
						settings.setMultiSelectionOnForPasswordProtection(true);
				}
				
			}
			
			if(e.getSource() instanceof JCheckBox) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				
				if(checkBox.getText().equals("Ask for confirmation before encrypting files")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.askConfirmationForEncryption(true);
					
					if(e.getStateChange() == ItemEvent.DESELECTED)
						settings.askConfirmationForEncryption(false);
				}
				
				if(checkBox.getText().equals("Ask for confirmation before decrypting files")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.askConfirmationForDecryption(true);
					
					if(e.getStateChange() == ItemEvent.DESELECTED) 
						settings.askConfirmationForDecryption(false);
				}
				
				if(checkBox.getText().equals("Ask for confirmation before password protecting files")) {
					if(e.getStateChange() == ItemEvent.SELECTED) 
						settings.askConfirmationForPasswordProtection(true);
					
					if(e.getStateChange() == ItemEvent.DESELECTED)
						settings.askConfirmationForPasswordProtection(false);
				}
			}
		}
		catch(IOException exception) {
			Messages.showGeneralError();
			return;
		}
	}
	
}
