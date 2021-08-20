package intray.launcher;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import intray.gui.mainframe.IntrayMainFrame;

public class Launcher {

	private static final boolean simulateFirstRun = false;

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			System.out.println("Change2");
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setLookAndFeel();
				new IntrayMainFrame(simulateFirstRun);

			}

		});
	}

}
