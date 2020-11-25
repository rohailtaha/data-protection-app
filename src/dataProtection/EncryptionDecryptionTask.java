package dataProtection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryptionTask implements Runnable
{
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORM = "AES";
	private static final String key = "You have no dogs";
	private final int cipherMode;
	private final boolean fileDeletionFlag;
	private final Path[] selectedFilesPaths;
	private final Path[] outputFilesPaths;
	private final Path outputFolderPath;
	private Cipher cipher;

	EncryptionDecryptionTask(int cipherMode, Path[] selectedFilesPaths, Path[] outputFilesPaths, Path outputFolderPath, boolean fileDeletionFlag)
	{
		this.cipherMode = cipherMode;
		this.selectedFilesPaths = selectedFilesPaths ;
		this.outputFilesPaths = outputFilesPaths;
		this.outputFolderPath = outputFolderPath;
		this.fileDeletionFlag = fileDeletionFlag;
		setParameters();
	}
	
	public void run() 
	{
		try {
			int i=0, lastIndexOfDot;
			for (Path selectedFilePath: selectedFilesPaths) {
				if(!Main.gui.progressFrame.isVisible() || selectedFilePath == null) 
					break;
				lastIndexOfDot = selectedFilePath.getFileName().toString().lastIndexOf(".");
				if(FileOperations.checkDuplicateFileNames(selectedFilePath.toFile())) 
					outputFilesPaths[i] = Paths.get(getPathString(selectedFilePath, outputFolderPath, lastIndexOfDot, true));
				else 
					outputFilesPaths[i] = Paths.get(getPathString(selectedFilePath, outputFolderPath, lastIndexOfDot, false));
				File inputFile = selectedFilePath.toFile();
				File outputFile = outputFilesPaths[i].toFile();

				FileInputStream inputStream = new FileInputStream(inputFile);
				FileOutputStream outputStream = new FileOutputStream(outputFile);

				byte[] inputBytes = new byte[(int) inputFile.length()];
				inputStream.read(inputBytes);
				byte[] outputBytes = cipher.doFinal(inputBytes);
				outputStream.write(outputBytes);

				inputStream.close();
				outputStream.close();
				
				UpdateProgressTask.setCurrentProgress((int) Files.size(inputFile.toPath()));
				if(fileDeletionFlag)
					FileOperations.deleteFile(inputFile.toPath());
				i++;
			}
		}
		catch (BadPaddingException | IllegalBlockSizeException | IllegalStateException exception) {
			Main.gui.progressFrame.setVisible(false);
			Messages.showCantEncryptDecryptError();
			return;
		}
		catch(NullPointerException | IOException exception) {
			Main.gui.progressFrame.setVisible(false);
			Messages.showGeneralError();
			return;
		}
	}
	
	private void setParameters()
	{
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			cipher = Cipher.getInstance(TRANSFORM);
			cipher.init(cipherMode, secretKey); 
		}
		catch(InvalidKeyException | NoSuchAlgorithmException| NoSuchPaddingException exception) {
			Messages.showGeneralError();
		}
	}
	
	private String getPathString(Path selectedFilePath, Path outputFolderPath, int lastIndexOfDot, boolean isDuplicate)
	{
		if(cipherMode == Cipher.ENCRYPT_MODE)
			return getPathStringForEncryption(selectedFilePath, outputFolderPath, lastIndexOfDot, isDuplicate);
		return getPathStringForDecryption(selectedFilePath, outputFolderPath, lastIndexOfDot, isDuplicate);			
	}
	
	private String getPathStringForEncryption(Path selectedFilePath, Path outputFolderPath, int lastIndexOfDot, boolean isDuplicate)
	{
		if(isDuplicate)
			return outputFolderPath.toString() + "\\" + selectedFilePath.getFileName().toString().substring(0, lastIndexOfDot) 
					+ " encrypted " + (FileOperations.getNumberOfDuplicates()[FileOperations.getIndexOfDuplicate()]+1) 
					+ selectedFilePath.getFileName().toString().substring(lastIndexOfDot);
		return outputFolderPath.toString() + "\\" + selectedFilePath.getFileName().toString().substring(0, lastIndexOfDot)
				+ " encrypted" + selectedFilePath.getFileName().toString().substring(lastIndexOfDot);
	}
	
	private String getPathStringForDecryption(Path selectedFilePath, Path outputFolderPath, int lastIndexOfDot, boolean isDuplicate)
	{
		if(isDuplicate)
			return outputFolderPath.toString() + "\\" + selectedFilePath.getFileName().toString().substring(0, lastIndexOfDot)
					+ "(decrypted " + (FileOperations.getNumberOfDuplicates()[FileOperations.getIndexOfDuplicate()] + 1)
					+ ")"  + selectedFilePath.getFileName().toString().substring(lastIndexOfDot);
		return outputFolderPath.toString() + "\\" + selectedFilePath.getFileName().toString().substring(0, lastIndexOfDot)
				+ " (decrypted)" + selectedFilePath.getFileName().toString().substring(lastIndexOfDot);
	}
	
}
