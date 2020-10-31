package intray.gui.entriesframe;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import intray.logic.IntrayController;
import intray.model.Entry;

public class EntryListFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	private boolean windowFocused;
	private EntryTablePanel entryTablePanel;
	private EntryInformationPanel entryInformationPanel;
	private JMenuBar menuBar;
	private IntrayController intrayController;

	public EntryListFrame(IntrayController intrayController) {
		this.setTitle("Entry List");
		this.intrayController = intrayController;
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension((int) (WIDTH / 1.2), (int) (HEIGHT / 1.2)));

		/*
		 * menu bar
		 */
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JCheckBoxMenuItem alwaysOnTop = new JCheckBoxMenuItem("Always On Top");
		alwaysOnTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EntryListFrame.this.setAlwaysOnTop(alwaysOnTop.isSelected());
			}
		});
		alwaysOnTop.setSelected(isAlwaysOnTop());

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EntryListFrame.this.setVisible(false);
			}
		});

		fileMenu.add(alwaysOnTop);
		fileMenu.add(exitItem);

		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		///////

		entryTablePanel = new EntryTablePanel(WIDTH);
		entryInformationPanel = new EntryInformationPanel();

		/*
		 * listeners
		 */
		ImageListener imageListener = createImageListener();
		FileListener fileListener = createFileListener();
		DeleteListener deleteListener = createDeleteListener();
		TableSelectionListener tableSelectionListener = createTableSelectionListener();

		/*
		 * Setting up entryTablePanel
		 */
		entryTablePanel.setData(intrayController.getEntries());
		entryTablePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		entryTablePanel.setTableSelectiontListener(tableSelectionListener);
		entryTablePanel.setImageListener(imageListener);
		entryTablePanel.setFileListener(fileListener);
		entryTablePanel.setDeleteListener(deleteListener);
		/*
		 * Setting up entryInformationPanel
		 */
		entryInformationPanel.setDeleteListener(deleteListener);
		entryInformationPanel.setImageListener(imageListener);
		entryInformationPanel.setFileListener(fileListener);

		/*
		 * laying out components
		 */

		add(entryInformationPanel, BorderLayout.SOUTH);
		add(entryTablePanel, BorderLayout.CENTER);

		/*
		 * windowFocused boolean is used by keep frame above toolbar thread in main
		 * frame
		 */
		addWindowFocusListener(new WindowFocusListener() {
			public void windowLostFocus(WindowEvent e) {
				windowFocused = false;
			}

			public void windowGainedFocus(WindowEvent e) {
				windowFocused = true;
			}
		});
		setLocationRelativeTo(null);

	}

	private TableSelectionListener createTableSelectionListener() {
		return new TableSelectionListener() {

			@Override
			public void onEntrySelected(int selectedEntryId) {
				Entry selectedEntry = intrayController.getEntry(selectedEntryId);
				if (entryInformationPanel.getCurrentlyViewedEntryId() != selectedEntryId && selectedEntryId != -1
						&& selectedEntry != null) {
					entryInformationPanel.setCurrentEntry(selectedEntry);
					entryInformationPanel.setTextArea(selectedEntry.getTitle());
					entryInformationPanel.setImageButtonEnabled(selectedEntry.isHasImage());
					entryInformationPanel.setOpenFileButtonEnabled(selectedEntry.isHasFile());
					entryInformationPanel.setOpenContainingFolderButtonEnabled(selectedEntry.isHasFile());
					entryInformationPanel.setDeleteButtonEnabled(true);
					entryInformationPanel.setCurrentlyViewedEntryId(selectedEntryId);
				} else if (selectedEntryId == -1) {
					entryInformationPanel.setCurrentEntry(null);
					entryInformationPanel.setTextArea("");
					entryInformationPanel.setImageButtonEnabled(false);
					entryInformationPanel.setOpenFileButtonEnabled(false);
					entryInformationPanel.setDeleteButtonEnabled(false);
				}

			}
		};
	}

	private DeleteListener createDeleteListener() {
		return new DeleteListener() {
			@Override
			public void deleteCurrentlySelectedEntry() {
				int deleteConfirm = JOptionPane.showConfirmDialog(entryTablePanel,
						"Are you sure you are done with the selected entry?", "Delete Entry",
						JOptionPane.YES_NO_OPTION);
				if (deleteConfirm == JOptionPane.YES_OPTION) {
					int selectedRow = entryTablePanel.getSelectedEntryId();
					intrayController.deleteEntry(selectedRow);
					try {
						intrayController.saveDataBase();
					} catch (IOException e) {
						e.printStackTrace();
					}
					entryTablePanel.refresh();
					entryTablePanel.selectEntry(0);
				}

			}

			@Override
			public void deleteCurrentlySelectedEntryNoConfirm() {
				int selectedRow = entryTablePanel.getSelectedEntryId();
				intrayController.deleteEntry(selectedRow);
				try {
					intrayController.saveDataBase();
				} catch (IOException e) {
					e.printStackTrace();
				}
				entryTablePanel.refresh();
				entryTablePanel.selectEntry(0);
			}

		};
	}

	private FileListener createFileListener() {
		return new FileListener() {
			@Override
			public boolean openFile() {

				int selectedEntryId = entryTablePanel.getSelectedEntryId();
				if (selectedEntryId != -1) {
					return intrayController.openFile(selectedEntryId);
				}
				return false;
			}

			@Override
			public void openContainingFolder() {
				int selectedEntryId = entryTablePanel.getSelectedEntryId();
				if (selectedEntryId != -1) {
					intrayController.openContainingFolderFromFilePath(selectedEntryId);
				}

			}

		};
	}

	private ImageListener createImageListener() {
		return new ImageListener() {
			@Override
			public boolean show() {
				int selectedEntryId = entryTablePanel.getSelectedEntryId();
				boolean hasImage = false;
				if (selectedEntryId != -1) {
					Entry selectedEntry = intrayController.getEntry(selectedEntryId);
					hasImage = selectedEntry.isHasImage();
					if (hasImage) {
						showImageDialogFromPath(selectedEntry.getImagePath());
					}
				}
				return hasImage;
			}
		};
	}

	private void showImageDialogFromPath(String imagePath) {
		JDialog imageDialog = new JDialog(this);
		ImageIcon icon = new ImageIcon(imagePath);
		JLabel imageLabel = new JLabel(icon);
		imageDialog.setMinimumSize(new Dimension(240, 100));
		imageDialog.setLocationRelativeTo(null);
		imageDialog.add(imageLabel);
		imageDialog.setSize(icon.getIconWidth(), icon.getIconHeight());
		imageDialog.setAlwaysOnTop(EntryListFrame.this.isAlwaysOnTop());
		imageDialog.setVisible(true);
	}

	public void refresh() {
		this.entryTablePanel.refresh();
		this.entryInformationPanel.reset();
		this.validate();
		this.repaint();
	}

	public void removeComponenetFromPanel(JPanel panel, Component component) {
		panel.remove(component);
		panel.validate();
		panel.repaint();
	}

	public boolean isEntryTableFocused() {
		return entryTablePanel.isEntryTableFocused();
	}

	public boolean isWindowFocused() {
		return windowFocused;
	}

}

class DummyFrame extends JFrame {

	private EntryListFrame entryListDialog;

	public DummyFrame(EntryListFrame entryListDialog, String title) {
		super(title);
		this.entryListDialog = entryListDialog;
		setToolBarIcon();
		setUndecorated(true);
		setVisible(true);
		setLocationRelativeTo(null);
//		setIconImage(icon);
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				entryListDialog.setVisible(false);
			}
		});
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				entryListDialog.requestFocus();
			}
		});

	}

	private void setToolBarIcon() {
		URL iconUrl = getClass().getResource("/intray_icon.png");
		ImageIcon icon = new ImageIcon(iconUrl);
		setIconImage(icon.getImage());
	}

	@Override
	public void dispose() {
		if (entryListDialog.isVisible()) {
			entryListDialog.setVisible(false);
		}
		super.dispose();
	}
}
