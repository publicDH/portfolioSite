package com.prims.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prims.Repository.FileData;

@Service
public class FileServices {

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

}
