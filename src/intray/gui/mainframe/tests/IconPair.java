package intray.gui.mainframe.tests;

import javax.swing.ImageIcon;

public class IconPair {
	String key;
	ImageIcon icon;

	public IconPair(String key, ImageIcon icon) {
		this.key = key;
		this.icon = icon;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
}
