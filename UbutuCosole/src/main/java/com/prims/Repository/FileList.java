package com.prims.Repository;

public class FileList {
	
	private String FileName;
	private String FilePath;
	private boolean isFile;
	
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

}
