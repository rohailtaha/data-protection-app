package dataProtection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.FileUtils;

public class FileOperations 
{
	private final static ArrayList<File> filesList = new ArrayList<File>(); 
	private static int[] numberOfDuplicates = new int[1000];
	private static int indexOfDuplicate;
	
	static File[] getFilesFromDirectory(File directory) throws ZipException, ArrayIndexOutOfBoundsException
	{
		List<File> files = FileUtils.getFilesInDirectoryRecursive(directory, false, false);
		
		File[] filesArr = new File[1000];
		int i=-1;
		for(File file:files) {
			if(file.isFile())
				filesArr[++i] = file;
		}
		return filesArr;
	}
	
	static Path[] getFilesPathsFromDirectory(File directory) throws ZipException, ArrayIndexOutOfBoundsException
	{
		List<File> files = FileUtils.getFilesInDirectoryRecursive(directory, false, false);
		
		Path[] filesPaths = new Path[1000];
		int i=-1;
		for(File file:files) {
			if(file.isFile())
				filesPaths[++i] = file.toPath();
		}
		return filesPaths;
	}
	
	static int totalFiles(Path directoryPath) throws ZipException
	{
		List<File> files = FileUtils.getFilesInDirectoryRecursive(directoryPath.toFile(), false, false);
		int fileCount = 0;
		for(File file:files) {
			if(file.isFile())
				++fileCount;
		}
		
		return fileCount;
	}
	
	static int getFilesSize(Path[] selectedFilesPaths) throws IOException
	{
		int size = 0;
		for(Path selectedFilePath:selectedFilesPaths) {
			if(selectedFilePath == null)
				break;
			size += (int) Files.size(selectedFilePath);
		}
		return size;
	}
	
	static boolean checkDuplicateFileNames(File file)
	{
			for(File _file:filesList) {
				if(file.getName().toString().equals(_file.getName().toString())) {
					++numberOfDuplicates[filesList.indexOf(_file)];
					indexOfDuplicate = filesList.indexOf(_file);
					filesList.add(file);
					return true;
				}
			}
			filesList.add(file);
			return false;
	}
	
	static void resetAttributes()
	{
		filesList.clear();
		numberOfDuplicates = new int[1000];
	}
	
	static void deleteFile(Path filePath)
	{
		filePath.toFile().delete();
	}

	public static int getIndexOfDuplicate() 
	{
		return indexOfDuplicate;
	}

	public static int[] getNumberOfDuplicates() 
	{
		return numberOfDuplicates;
	}
}
