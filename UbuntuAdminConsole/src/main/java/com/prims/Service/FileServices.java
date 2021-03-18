package com.prims.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prims.Repository.FileData;

@Service
public class FileServices {

	public static String MasterDirectory = "/home/ubuntu/projectTest";
	public static String DirectorySplit = "/";
	public static String DirectorySplitDouble = "/";

	public String getDirectorySplitDouble() {
		return DirectorySplitDouble;
	}

	public void setDirectorySplitDouble(String directorySplitDouble) {
		DirectorySplitDouble = directorySplitDouble;
	}

	public String getMasterDirectory() {
		return MasterDirectory;
	}

	public void setMasterDirectory(String masterDirectory) {
		MasterDirectory = masterDirectory;
	}

	public String getDirectorySplit() {
		return DirectorySplit;
	}

	public void setDirectorySplit(String directorySplit) {
		DirectorySplit = directorySplit;
	}

	public List<FileData> getFiles(String Path) {

		List<FileData> Files = new ArrayList<FileData>();

		File file = new File(Path);

		// 1. check if the file exists or not
		boolean isExists = file.exists();

		if (!isExists) {
			System.out.println("There is nothing.");
		}

		// 2. check if the object is directory or not.
		if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			for (File tFile : fileList) {

				Files.add(new FileData(tFile.getName(), tFile.getPath(), tFile.isFile()));

			}
		} else {
			System.out.println("It is not a directory.");
		}

		return Files;

	}

	public void makeDirectory(String Path) {
		File md = new File(Path);
		if (!md.exists()) {
			md.mkdir();
		}
	}

	public void deleteFile(String Path) {
		File df = new File(Path);
		if (df.exists())
			df.delete();
	}

	public void deleteForder(String Path) {

		File deleteFolder = new File(Path);

		if (deleteFolder.exists()) {
			File[] deleteFolderList = deleteFolder.listFiles();

			for (int i = 0; i < deleteFolderList.length; i++) {
				if (deleteFolderList[i].isFile()) {
					deleteFolderList[i].delete();
				} else {
					deleteFile(deleteFolderList[i].getPath());
				}
				deleteFolderList[i].delete();
			}
			deleteFolder.delete();
		}
	}
}
