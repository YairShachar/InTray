package intray.gui.mainframe;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class IntraySettingsMenu extends JPopupMenu {
	private static final long serialVersionUID = 10L;
	private IntrayMainFrame intrayMainFrame;
	private Vector<Component> menuItemsVector;
	private JMenuItem reportABug;
	private JMenuItem about;
	private JMenuItem minimizeToTray;
	private JMenuItem item;
	private JCheckBoxMenuItem isBlendedCheckBox;
	private JCheckBoxMenuItem isLockedCheckBox;
	private EntriesFrameListener entriesFrameListener;

	public IntraySettingsMenu(IntrayMainFrame intrayMainFrame, EntriesFrameListener entriesFrameListener) {
		this.intrayMainFrame = intrayMainFrame;
		this.entriesFrameListener = entriesFrameListener;
		menuItemsVector = new Vector<Component>();
		addNewComponent(createLockCheckBoxItem());
		addNewComponent(createIsBlendedCheckBoxItem());
		addSeparator();
		addNewComponent(createOpenCurrentEntriesMenuItem());
		addSeparator();
		JMenu help = new JMenu("Help");
		help.add(createAboutItem());
		help.add(createReportABugMenuItem());
		addNewComponent(help);
		addSeparator();
		addNewComponent(createPreferencesMenuItem());
		addSeparator();
		addNewComponent(createMinimize());
		addNewComponent(createExitItem());

		addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				isLockedCheckBox.setSelected(intrayMainFrame.isLocked());
				isBlendedCheckBox.setSelected(intrayMainFrame.isBlended());
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
	}

	private JMenuItem createPreferencesMenuItem() {
		item = new JMenuItem("Preferences...");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				intrayMainFrame.getSettingsDialog().setVisible(true);
			}
		});

		return item;

	}

	private JCheckBoxMenuItem createLockCheckBoxItem() {
		isLockedCheckBox = new JCheckBoxMenuItem("Locked");
		isLockedCheckBox.setSelected(!intrayMainFrame.isDraggable());
		isLockedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isLockedCheckBox.setSelected(isLockedCheckBox.isSelected());
				intrayMainFrame.setLocked(isLockedCheckBox.isSelected());
			}
		});
		return isLockedCheckBox;
	}

	private JCheckBoxMenuItem createIsBlendedCheckBoxItem() {
		isBlendedCheckBox = new JCheckBoxMenuItem("Blended");
		isBlendedCheckBox.setSelected(intrayMainFrame.isBlended());
		isBlendedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				intrayMainFrame.setBlended(isBlendedCheckBox.isSelected());
			}
		});
		return isBlendedCheckBox;
	}

	private JMenuItem createMinimize() {
		this.minimizeToTray = new JMenuItem("Minimize");

		minimizeToTray.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				intrayMainFrame.setExtendedState(Frame.ICONIFIED);
			}

		});
		return minimizeToTray;
	}

	private void addNewComponent(Component c) {
		menuItemsVector.add(c);
		this.add(c);
	}

	private JMenuItem createAboutItem() {

		this.about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(intrayMainFrame,
						"The in-tray is designed to help you capture thoughts, ideas, photos, files as they pop-up\r\ncontact us at: intray.contact@gmail.com\r\n"
								+ "");
			}
		});
		return this.about;

	}

	private JMenuItem createOpenCurrentEntriesMenuItem() {
		JMenuItem item = new JMenuItem("Current Entries...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				entriesFrameListener.openWindow();
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		return item;
	}

	private JMenuItem createExitItem() {
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (WindowListener w : intrayMainFrame.getWindowListeners()) {
					w.windowClosing(new WindowEvent(intrayMainFrame, 0));
				}
			}
		});
		return exitItem;
	}

	private JMenuItem createReportABugMenuItem() {
		this.reportABug = new JMenuItem("Report A Bug...");
		reportABug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(intrayMainFrame,
						"Kindly report to intray.reportabug@gmail.com\r\n" + "Thanks!");
			}
		});
		return this.reportABug;
	}

}