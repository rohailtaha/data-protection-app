package dataProtection;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Settings implements Serializable
{
	private boolean DEL_ORIGINAL_FILES_AFTER_ENCRYPTION = false;
	private boolean MULTI_SELECTION_ON_FOR_ENCRYPTION = true;
	private boolean DEL_ENCRYPTED_FILES_AFTER_DECRYPTION = false;
	private boolean MULTI_SELECTION_ON_FOR_DECRYPTION = true;
	private boolean DEL_ORIGINAL_FILES_AFTER_PP = false;   // PP --> Password Protection
	private boolean MULTI_SELECTION_ON_FOR_PP = true;
	private boolean ASK_ENCRYPTION_CONFIRMATION = true;
	private boolean ASK_DECRYPTION_CONFIRMATION = true;
	private boolean ASK_PASSWORD_PROTECTION_CONFIRMATION = true;  
	private static transient ObjectInputStream ois;
	private static transient ObjectOutputStream oos;

	private Path getSettingsFilePath() 
	{
			return Paths.get("settings.set");
	}

	void setDelModeAfterEncryption(boolean choice) throws FileNotFoundException, IOException
	{
		DEL_ORIGINAL_FILES_AFTER_ENCRYPTION = choice;
	}
	
	boolean isDelModeAfterEncryptionOn() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return  settings.DEL_ORIGINAL_FILES_AFTER_ENCRYPTION;
		}
		return false;
	}

	void setDelModeAfterDecryption(boolean choice) throws FileNotFoundException, IOException
	{
		DEL_ENCRYPTED_FILES_AFTER_DECRYPTION = choice;
	}
	
	boolean isDelModeAfterDecryptionOn() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.DEL_ENCRYPTED_FILES_AFTER_DECRYPTION;
		}
		return false;
	}
	
	void setDelModeAfterPP(boolean choice)
	{
		DEL_ORIGINAL_FILES_AFTER_PP = choice;
	}

	boolean isDelModeAfterPPOn() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.DEL_ORIGINAL_FILES_AFTER_PP;
		}
		return false;
	}
	
	void setMultiSelectionOnForEncryption(boolean choice)  
	{
		MULTI_SELECTION_ON_FOR_ENCRYPTION = choice;
	}
	
	boolean isMultiSelectionOnForEncryption() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.MULTI_SELECTION_ON_FOR_ENCRYPTION;
		}
		return true;
	}

	void setMultiSelectionOnForDecryption(boolean choice)  
	{
		MULTI_SELECTION_ON_FOR_DECRYPTION = choice;
	}

	boolean isMultiSelectionOnForDecryption() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.MULTI_SELECTION_ON_FOR_DECRYPTION;
		}
		return true;
	}

	void setMultiSelectionOnForPasswordProtection(boolean choice)  
	{
		MULTI_SELECTION_ON_FOR_PP = choice;
	}
	
	boolean isMultiSelectionOnForPasswordProtection() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.MULTI_SELECTION_ON_FOR_PP;
		}
		return true;
	}
	
	void askConfirmationForEncryption(boolean choice)
	{
		ASK_ENCRYPTION_CONFIRMATION = choice;
	}
	
	boolean isEncryptionConfirmationOn() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.ASK_ENCRYPTION_CONFIRMATION;
		}
		return true;
	}
	
	void askConfirmationForDecryption(boolean choice)
	{
		ASK_DECRYPTION_CONFIRMATION = choice;
	}

	boolean isDecryptionConfirmationOn() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.ASK_DECRYPTION_CONFIRMATION;
		}
		return true;
	}
	
	void askConfirmationForPasswordProtection(boolean choice)
	{
		ASK_PASSWORD_PROTECTION_CONFIRMATION = choice;
	}
	
	boolean isPasswordProtectionConfirmationOn() throws IOException, ClassNotFoundException
	{
		if(isReadable()) {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			return settings.ASK_PASSWORD_PROTECTION_CONFIRMATION;
		}
		return true;
	}
	
	void setSavedSettings() throws ClassNotFoundException
	{
		try {
			ois = new ObjectInputStream(Files.newInputStream(getSettingsFilePath()));
			Settings settings = (Settings) ois.readObject();
			ois.close();
			if (settings == null)
				return;
			
			if(settings.DEL_ORIGINAL_FILES_AFTER_ENCRYPTION) 
				Main.gui.deleteOriginalBtn.setSelected(true);
			else
				Main.gui.keepOriginalBtn.setSelected(true);
			
			if(!(settings.MULTI_SELECTION_ON_FOR_ENCRYPTION))
				Main.gui.singleEncryptionBtn.setSelected(true);
			else
				Main.gui.multipleEncryptionBtn.setSelected(true);
			
			if(settings.DEL_ENCRYPTED_FILES_AFTER_DECRYPTION)
				Main.gui.deleteEncryptedBtn.setSelected(true);
			else
				Main.gui.keepEncryptedBtn.setSelected(true);
			
			if(!(settings.MULTI_SELECTION_ON_FOR_DECRYPTION))
				Main.gui.singleDecryptionBtn.setSelected(true);
			else
				Main.gui.multipleDecryptionBtn.setSelected(true);
			
			if(settings.DEL_ORIGINAL_FILES_AFTER_PP)
				Main.gui.deletePPOriginalBtn.setSelected(true);
			else
				Main.gui.keepPPOriginalBtn.setSelected(true);
			
			if(!(settings.MULTI_SELECTION_ON_FOR_PP))
				Main.gui.singlePPBtn.setSelected(true);
			else
				Main.gui.multiplePPBtn.setSelected(true);
			
			if(settings.ASK_ENCRYPTION_CONFIRMATION)
				Main.gui.encryptionConfirmationCheck.setSelected(true);
			else
				Main.gui.encryptionConfirmationCheck.setSelected(false);
			
			if(settings.ASK_DECRYPTION_CONFIRMATION)
				Main.gui.decryptionConfirmationCheck.setSelected(true);
			else
				Main.gui.decryptionConfirmationCheck.setSelected(false);
			
			if(settings.ASK_PASSWORD_PROTECTION_CONFIRMATION)
				Main.gui.ppConfirmationCheck.setSelected(true);
			else
				Main.gui.ppConfirmationCheck.setSelected(false);
			
		}
		catch (EOFException exception) {
			try {  // if settings file is empty.
				saveDefaultSettings();
				setDefaultSettings();
			}
			catch (IOException exception2) {
				Messages.showGeneralError();
			}
		}
		catch (IOException exception) {
			Messages.showGeneralError();
		}
	}
	
	void setDefaultSettings() 
	{
		if(!DEL_ORIGINAL_FILES_AFTER_ENCRYPTION)
			Main.gui.keepOriginalBtn.setSelected(true);
		else
			Main.gui.deleteOriginalBtn.setSelected(true);
		
		if(MULTI_SELECTION_ON_FOR_ENCRYPTION)
			Main.gui.multipleEncryptionBtn.setSelected(true);
		else
			Main.gui.singleEncryptionBtn.setSelected(true);
		
		if(!DEL_ENCRYPTED_FILES_AFTER_DECRYPTION)
			Main.gui.keepEncryptedBtn.setSelected(true);
		else
			Main.gui.deleteEncryptedBtn.setSelected(true);
		
		if(MULTI_SELECTION_ON_FOR_DECRYPTION)
			Main.gui.multipleDecryptionBtn.setSelected(true);
		else
			Main.gui.singleDecryptionBtn.setSelected(true);
		
		if(!DEL_ORIGINAL_FILES_AFTER_PP)
			Main.gui.keepPPOriginalBtn.setSelected(true);
		else
			Main.gui.deletePPOriginalBtn.setSelected(true);
		
		if(MULTI_SELECTION_ON_FOR_PP) 
			Main.gui.multiplePPBtn.setSelected(true);
		else
			Main.gui.singlePPBtn.setSelected(true);
		
		if(ASK_ENCRYPTION_CONFIRMATION)
			Main.gui.encryptionConfirmationCheck.setSelected(true);
		else
			Main.gui.encryptionConfirmationCheck.setSelected(false);
			
		if(ASK_DECRYPTION_CONFIRMATION)
			Main.gui.decryptionConfirmationCheck.setSelected(true);
		else
			Main.gui.decryptionConfirmationCheck.setSelected(false);

		if(ASK_PASSWORD_PROTECTION_CONFIRMATION)
			Main.gui.ppConfirmationCheck.setSelected(true);
		else
			Main.gui.ppConfirmationCheck.setSelected(false);
	}
	
	void saveChangedSettings() throws IOException
	{
		oos = new ObjectOutputStream(Files.newOutputStream(getSettingsFilePath()));
		oos.writeObject(ClickController.settings);
		oos.close();
	}
	
	void saveDefaultSettings() throws IOException
	{
		Settings settings = new Settings();
		oos = new ObjectOutputStream(Files.newOutputStream(getSettingsFilePath()));
		oos.writeObject(settings);
		oos.close();
	}
	
	boolean isReadable() throws IOException   // checks if the settings file is empty or not.
	{
		return (Files.size(getSettingsFilePath()) > 0);   
	}
}
