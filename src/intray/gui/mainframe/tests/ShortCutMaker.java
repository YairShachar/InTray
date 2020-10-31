package intray.gui.mainframe.tests;

import net.jimmc.jshortcut.JShellLink;

/*
 * 
 * 
 * Referenced from:
 * https://stackoverflow.com/questions/1496343/how-to-create-shortcut-icon-for-java-program#:~:text=%20%20%201%20right%20click%20on%20desktop,icon%20and%20change%20icon%20of%20file%20More%20
 * 
 * Jar used: 
 * https://github.com/jimmc/jshortcut/downloads
 * 
 * 
 * (Thank you! :) )
 */

public class ShortCutMaker {
	JShellLink link;
	String filePath;

	public ShortCutMaker(String path) {
		try {
			link = new JShellLink();
			filePath = path;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createDesktopShortcut() {
		try {
			link.setFolder(JShellLink.getDirectory("desktop"));
			link.setName("Intray");
			link.setPath(filePath);
			link.save();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}