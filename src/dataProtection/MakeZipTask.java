package dataProtection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class MakeZipTask implements Runnable
{
	private static String password;
	private ZipFile zipFile;
	private String zipFileName;
	private ZipParameters par;
	private final Path[] selectedFilesPaths;
	private final Path outputFolderPath;
	private final boolean fileDeletionFlag;
	
	MakeZipTask(Path[] selectedFilesPaths, Path outputFolderPath, boolean fileDeletionFlag) 
	{
		this.selectedFilesPaths = selectedFilesPaths;
		this.outputFolderPath = outputFolderPath;
		this.fileDeletionFlag = fileDeletionFlag;
		setZipFileName();
		setParameters();
		makeZipFile();
	}

	public void run() 
	{
		try {
			for (Path selectedFilePath : selectedFilesPaths) {
				if(!Main.gui.progressFrame.isVisible() || selectedFilePath == null) 
					break;
			
				zipFile.addFile(selectedFilePath.toFile(), par);  
				UpdateProgressTask.setCurrentProgress((int) Files.size(selectedFilePath));
				
				if(fileDeletionFlag)
					FileOperations.deleteFile(selectedFilePath);
			}
		}
		catch(IOException exception) {
			Main.gui.progressFrame.setVisible(false);
			Messages.showGeneralError();
			return;
		}
	}
	
	private void setParameters()
	{
		par = new ZipParameters();
		par.setEncryptFiles(true);
		par.setEncryptionMethod(EncryptionMethod.AES);
		par.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256); 
		par.setCompressionMethod(CompressionMethod.STORE);
	}
	
	private void makeZipFile()
	{
		zipFile = new ZipFile(zipFileName, password.toCharArray());
	}
	
	private void setZipFileName()
	{
		int lastIndex;
		if(selectedFilesPaths[0].toFile().isDirectory())
			lastIndex = selectedFilesPaths[0].getFileName().toString().length();
		else
			lastIndex = selectedFilesPaths[0].getFileName().toString().lastIndexOf(".");
		zipFileName =  outputFolderPath + "\\" + selectedFilesPaths[0].getFileName().toString().substring(0,lastIndex) 
				+ " protected.zip";
	}
	
	static void setPassword(String pass) 
	{
		password = pass;
	}
}
