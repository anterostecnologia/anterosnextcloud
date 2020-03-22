package br.com.anteros.nextcloud.api.webdav;

import java.util.Date;

public class FolderItemDetail {

	private Long contentLength;
	private String contentType;
	private Date creation;
	private Date modified;
	private String displayName;
	private boolean isDirectory;
	private String path;
	private String name;
	
	public Long getContentLength() {
		return contentLength;
	}
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Date getCreation() {
		return creation;
	}
	public void setCreation(Date creation) {
		this.creation = creation;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isDirectory() {
		return isDirectory;
	}
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
