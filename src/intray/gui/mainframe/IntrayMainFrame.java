package intray.gui.mainframe;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import intray.gui.dialog.preferences.IntrayPrefsDialog;
import intray.gui.entriesframe.EntryListFrame;
import intray.gui.entriesframe.PrefsListener;
import intray.gui.listeners.defaults.DefaultEntriesDialogListener;
import intray.gui.mainframe.panel.submitentry.EntryEvent;
import intray.gui.mainframe.panel.submitentry.IntraySubmitEntryPanel;
import intray.gui.mainframe.panel.submitentry.IntraySubmitEntryPanel.SubmitEntryPanelComponenet;
import intray.logic.IntrayConstants;
import intray.logic.IntrayController;

public class IntrayMainFrame extends JFrame {
	private IntrayController intrayController;
	private String currentCategory = "general";
	private EntryListFrame entryListDialog = null;
	private IntraySubmitEntryPanel submitEntryPanel;
	private IntraySettingsMenu rightClickMenu;
	private IntrayMainFrameDragger frameDragger;
	private EntriesFrameListener entriesFrameListener = new DefaultEntriesDialogListener(this);
	private IntrayPrefsDialog prefsDialog;
	private static final String INTRAY_ICON = "/intray_icon.png";
	private static final long serialVersionUID = 1L;
	private static final String intrayVersion = "1.0.4";
	private static String workingDir = null;
	private Preferences prefs;
	private int coord_x = -1;
	private int coord_y = -1;
	private boolean isLocked;
	private boolean isBlended;
	private IntrayMainFrameDragger frameDraggerFromSubmitButton;

	public IntrayMainFrame(boolean simulateFirstRun) {
		super("In-Tray");
		prefsDialog = new IntrayPrefsDialog(this);
		prefs = Preferences.userRoot().node("intray.prefs" + intrayVersion);
		if (simulateFirstRun) {
			prefs.put("workingDir", "");
			prefs.putInt("coord_x", -1);
			prefs.putInt("coord_y", -1);
			prefs.putBoolean("isLocked", false);
		}
		setLayout(new FlowLayout());
		setCloseOperation();
		if (SystemTray.isSupported()) {
			initTrayIcon();
		}
		initSubmitEntryPanel();
		setDefaultButton();
		initRightClickPopupMenu();
		setToolBarIcon();
		setAlwaysOnTop(true);
		setUndecorated(true);
		pack();
		setBackground(new Color(0, 0, 0, 0));
		setDefaultsFromPreferences();
		initIntrayController();
		initDraggable();
		setVisible(true);
		entryListDialog = new EntryListFrame(intrayController);
	}

	public void setCurrentEntriesFrame(EntryListFrame epf) {

		this.entryListDialog = epf;
	}

	public void initRightClickPopupMenu() {
		rightClickMenu = new IntraySettingsMenu(this, entriesFrameListener);
		addMouseListener(createSettingsRightClickPopupListener());

	}

	public String getCurrentCategory() {
		return currentCategory;
	}

	public EntryListFrame getCurrentEntriesFrame() {
		return entryListDialog;
	}

	public void setCurrentCategory(String string) {
		this.currentCategory = string;
	}

	public IntrayController getController() {
		return this.intrayController;
	}

	public IntrayMainFrame getIntrayMainFrame() {
		return this;
	}

	public boolean isDraggable() {
		if (this.frameDragger != null) {
			return this.frameDragger.isActive();
		}
		return false;

	}

	public void setLocked(boolean b) {
		this.isLocked = b;
		if (frameDragger != null) {
			frameDragger.setActive(!b);
		}
		if (frameDraggerFromSubmitButton != null) {
			frameDraggerFromSubmitButton.setActive(!b);
		}

		prefs.putBoolean("isLocked", b);

	}

	public void openNewEntriesFrame() {
		entryListDialog = new EntryListFrame(intrayController);
	}

	public JDialog getSettingsDialog() {
		return this.prefsDialog;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void updatePreferencesLocation(int coord_x, int coord_y) {
		prefs.putInt("coord_x", coord_x);
		prefs.putInt("coord_y", coord_y);
	}

	public void updatePreferencesisLocked(boolean isLocked) {
		prefs.putBoolean("isLocked", isLocked);
	}

	public void setBlended(boolean b) {
		if (b) {
			submitEntryPanel.setVisible(false);
			submitEntryPanel.setBackground(new Color(0, 0, 0, 0));
			submitEntryPanel.repaint();
			submitEntryPanel.validate();
			submitEntryPanel.setVisible(true);
			submitEntryPanel.setFocusOnTextfield();
		} else {
			submitEntryPanel.setVisible(false);
			submitEntryPanel.setBackground(submitEntryPanel.getDefaultBackgroundColor());
			submitEntryPanel.repaint();
			submitEntryPanel.validate();
			submitEntryPanel.setVisible(true);
			submitEntryPanel.setFocusOnTextfield();

		}
		isBlended = b;
		updatePreferencesisBlended(isBlended);
	}

	public void updatePreferencesisBlended(boolean isBlended) {
		prefs.putBoolean("isBlended", isBlended);
	}

	public boolean isBlended() {
		return isBlended;
	}

	public boolean isLocked() {
		return this.isLocked;
	}

	private void setCloseOperation() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("MainFrame is closing.");
				dispose();
				System.gc();
				System.exit(0);
			}
		});
	}

	private void initIntrayController() {
		this.intrayController = new IntrayController(this);
	}

	private void setDefaultsFromPreferences() {
		prefsDialog.setPrefsListener(new PrefsListener() {
			public void preferencesSet(String workingDir) {
				System.out.println("IntrayMainFrame: preferences listener: workingDir = " + workingDir);
				IntrayMainFrame.workingDir = workingDir;
				prefs.put("workingDir", workingDir);
				if (intrayController != null) {
					restartIntrayController();
					if (entryListDialog != null) {
						entryListDialog.refresh();
					}
				}

			}
		}

		);

		workingDir = prefs.get("workingDir", "");
		if (workingDir.equals("")) {
			startFirstRun();
		}
		prefsDialog.setDefaults(workingDir);

		this.coord_x = prefs.getInt("coord_x", -1);
		this.coord_y = prefs.getInt("coord_y", -1);
		if (coord_x != -1 && coord_y != -1) {
			setLocation(coord_x, coord_y);
		} else {
			setLocationRelativeTo(null);
		}
		boolean startLocked = prefs.getBoolean("isLocked", false);
		setLocked(startLocked);
		boolean startBlended = prefs.getBoolean("isBlended", false);
		setBlended(startBlended);
		this.isBlended = startBlended;

	}

	private void startFirstRun() {

		JOptionPane.showConfirmDialog(this,
				"We really appreciate you trying out the alpha version of the intray, we'd love to receive feedback at intray.contact@gmail.com",
				"Hi! :)", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
		int init = JOptionPane.showConfirmDialog(this,
				"Before we begin, we need a directory to store your 'current entries', your intray executable file will be moved to this folder.",
				"Intray installation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (init != JOptionPane.OK_OPTION) {
			System.exit(0);
		}
		JFileChooser firstRunFileChooser = new JFileChooser();
		firstRunFileChooser.setDialogTitle("Intray installation.");
		firstRunFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		firstRunFileChooser.setAcceptAllFileFilterUsed(false);
		if (firstRunFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File chosenFolder = firstRunFileChooser.getSelectedFile();
			try {
				Files.createDirectories(Paths.get(chosenFolder.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
			JOptionPane.showConfirmDialog(this,
					"Thanks, your entries will be stored in: " + chosenFolder.getAbsolutePath()
							+ " (this can be changed at anytime through the intray preferences)",
					"Intray installation", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
			System.out.println("folder chosen: " + chosenFolder.getAbsolutePath());
			workingDir = chosenFolder.getAbsolutePath();

		} else {
			System.exit(0);
		}
	}

	private void restartIntrayController() {
		try {
			intrayController.loadDataBase(Paths.get(workingDir).resolve(IntrayConstants.DATABASE_FILE_NAME).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initTrayIcon() {
		SystemTray tray = SystemTray.getSystemTray();
		final URL resource = getClass().getResource(INTRAY_ICON);
		TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(resource), "Intray");
		icon.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if (arg0.getClickCount() == 2) {
					forceFrameVisible();
					IntrayMainFrame.this.setVisible(true);
				}
			}
		});

		try {
			tray.add(icon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void forceFrameVisible() {
		int timesToAskForVisibility = 10;
		while (isInTaskBarArea() && timesToAskForVisibility > 0) {
			if (this.entryListDialog != null && this.entryListDialog.isWindowFocused()) {
				break;
			}
			setVisible(true);
			if (!submitEntryPanel.isTextFieldFocusedByUser()) {

				IntrayMainFrame.this.requestFocus();
			}
			timesToAskForVisibility--;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean isInTaskBarArea() {
		if (!isVisible()) {
			return false;
		}
		int epsilon = 40;
		Point currentLocation = getLocationOnScreen();
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int taskbarLocation_y = winSize.height;
		if (currentLocation.y >= Math.abs(taskbarLocation_y - epsilon)) {
			return true;
		}
		return false;
	}

	private void setToolBarIcon() {
		URL iconUrl = getClass().getResource("/intray_icon.png");
		ImageIcon icon = new ImageIcon(iconUrl);
		setIconImage(icon.getImage());
	}

	private void initDraggable() {
		this.frameDragger = new IntrayMainFrameDragger(this);
		this.frameDraggerFromSubmitButton = new IntrayMainFrameDragger(this, -150, 0);
		addMouseListener(frameDragger);
		addMouseMotionListener(frameDragger);
		submitEntryPanel.getSubmitEntryButton().addMouseListener(frameDraggerFromSubmitButton);
		submitEntryPanel.getSubmitEntryButton().addMouseMotionListener(frameDraggerFromSubmitButton);
		frameDraggerFromSubmitButton.setActive(true);
		frameDragger.setActive(true);

	}

	private void setDefaultButton() {
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				"clickButton");
		getRootPane().getActionMap().put("clickButton", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				submitEntryPanel.getSubmitEntryButton().doClick();
			}
		});

	}

	private void initSubmitEntryPanel() {
		this.submitEntryPanel = new IntraySubmitEntryPanel(entriesFrameListener);
		submitEntryPanel.addMouseListener(SubmitEntryPanelComponenet.TEXT_FIELD,
				createSettingsRightClickPopupListener());
		submitEntryPanel.setLockListener(new LockListener() {
			@Override
			public void lock() {
				IntrayMainFrame.this.setLocked(!IntrayMainFrame.this.isLocked());

			}
		});
		submitEntryPanel.setEntryEventListener(SubmitEntryPanelComponenet.TEXT_FIELD, new SubmitEntryEventListener() {
			public boolean submit(EntryEvent event) {
				boolean success = intrayController.submitAndSaveEntryEvent(event);
				if (success) {
					try {
						intrayController.saveDataBase();
					} catch (IOException e) {
						e.printStackTrace();
					}
					EntryListFrame currentEntriesFrame = getCurrentEntriesFrame();
					if (currentEntriesFrame != null) {
						if (currentEntriesFrame.isVisible()) {
							currentEntriesFrame.refresh();
						}
					}
					return true;
				} else
					return false;

			}
		});

		this.add(submitEntryPanel);
	}

	private MouseAdapter createSettingsRightClickPopupListener() {
		return new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me)) {
					showPopup(me);
				}
			}

			void showPopup(MouseEvent me) {
				if (me.isPopupTrigger())
					rightClickMenu.show(me.getComponent(), me.getX(), me.getY());
			}
		};
	}

}
