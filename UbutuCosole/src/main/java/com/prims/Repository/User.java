package com.prims.Repository;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
	
	private String id;
	private String Password;
	private String directory;
	private boolean isAdmin;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setPassword(String password) {
		Password = password;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	/*
	 * @Override public String toString() { return "id=" + id + " pwd=" + Password +
	 * " directory=" + directory + " isAdmin=" + isAdmin;
	 * 
	 * }
	 */
	
}
