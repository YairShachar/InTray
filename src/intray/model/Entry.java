package intray.model;

import java.io.Serializable;

public class Entry implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private String imagePath;
	private String relatedFilePath;

	private boolean hasImage;
	private boolean hasFile;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
		setHasImage(true);
	}

	public String getRelatedFilePath() {
		return relatedFilePath;
	}

	public void setRelatedFilePath(String relatedFilePath) {
		this.relatedFilePath = relatedFilePath;
		setHasFile(true);
	}

	public boolean isHasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}

	public boolean isHasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}

}
