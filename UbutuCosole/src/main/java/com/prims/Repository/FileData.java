package com.prims.Repository;

public class FileData implements Comparable<FileData> {
	
	private String FileName;
	private String FilePath;
	private boolean isFile;
	
	public FileData(String FileName, String FilePath, boolean isFile) {
		this.FileName = FileName;
		this.FilePath = FilePath;
		this.isFile = isFile;
	}
	
	public String getFileName() {
		return FileName;
	}
	
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	
	public String getFilePath() {
		return FilePath;
	}
	
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	
	public boolean isFile() {
		return isFile;
	}
	
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	@Override
	public int compareTo(FileData o) {

		if(this.isFile == false) {
			return -1;
		}else if(this.isFile != false && o.isFile == false)
			return 1;
		else
		
		return 0;
	}

}
