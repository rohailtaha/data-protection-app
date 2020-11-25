package dataProtection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFileChooser;

import net.lingala.zip4j.exception.ZipException;

public class FilesSelection 
{
	private JFileChooser fileChooser;
	
	void setFileChooserParameters(String title, String function, int fileSelectionMode) throws IOException, ClassNotFoundException
	{
		fileChooser = new JFileChooser();
		if(function.equals("encryption"))
			fileChooser.setMultiSelectionEnabled(ClickController.settings.isMultiSelectionOnForEncryption());
		
		else if(function.equals("decryption"))
			fileChooser.setMultiSelectionEnabled(ClickController.settings.isMultiSelectionOnForDecryption());
			
		else if(function.equals("password protection")) 
			fileChooser.setMultiSelectionEnabled(ClickController.settings.isMultiSelectionOnForPasswordProtection());
			
		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(fileSelectionMode);
	}
	
	Path[] chooseFiles() throws ZipException, ArrayIndexOutOfBoundsException
	{
	
		int choice = fileChooser.showDialog(Main.gui, "Select");
		if(choice == JFileChooser.CANCEL_OPTION)
			return null;
		
		if(!(fileChooser.isMultiSelectionEnabled())) {                   // Single selection
			File selectedFile = fileChooser.getSelectedFile();	
			if(selectedFile.isDirectory()) {               
				if(FileOperations.totalFiles(selectedFile.toPath()) == 1)
					return FileOperations.getFilesPathsFromDirectory(selectedFile);
				else if(FileOperations.totalFiles(selectedFile.toPath()) == 0) {
					Messages.showEmptyDirectoryError();
					return null;
				}
				else if(FileOperations.totalFiles(selectedFile.toPath()) > 1) {
					Messages.showMultipleFilesError();
					return null;
				}
			}
				Path[] selectedFilePath =  {selectedFile.toPath()};
				return selectedFilePath;
		}
		
		else {
			File[] selectedFiles = fileChooser.getSelectedFiles();       // Multiple selection
			Path[] selectedFilesPaths = new Path[1000];
			int i = -1;
			for (File selectedFile:selectedFiles) {
				if(selectedFile.isDirectory()) {
					 File[] filesinDirectory = FileOperations.getFilesFromDirectory(selectedFile);
					for(File file: filesinDirectory) {
						if(file == null)
							break;
						selectedFilesPaths[++i] = file.toPath();
					}
					continue;
				}
				selectedFilesPaths[++i] = selectedFile.toPath();
			}
			if(selectedFilesPaths[0] == null) { // If no files are found inside the selected directories.
				Messages.showEmptyDirectoryError();
				return null;
			}
			
			return selectedFilesPaths;
		}
	}
	
	Path chooseFilesDestination() throws IOException, ClassNotFoundException
	{	
		int result = fileChooser.showDialog(Main.gui, "Select");
		if(result == JFileChooser.CANCEL_OPTION)
			return null;
		
		return fileChooser.getSelectedFile().toPath();
	}
	
	//Overloaded method for setting parameters for file chooser that selects destination folder
	void setFileChooserParameters(String title, int fileSelectionMode) throws IOException, ClassNotFoundException
	{
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(fileSelectionMode);
	}
}
