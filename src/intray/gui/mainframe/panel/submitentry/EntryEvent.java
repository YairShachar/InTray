package intray.gui.mainframe.panel.submitentry;

import java.awt.image.BufferedImage;

public class EntryEvent {
	private EntryEventType type;
	private String title;
	private String format;
	private BufferedImage image;
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EntryEvent(EntryEventType type, String title) {
		this.title = title;
		this.type = type;
	}

	public EntryEventType getType() {
		return type;
	}

	public void setType(EntryEventType type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
