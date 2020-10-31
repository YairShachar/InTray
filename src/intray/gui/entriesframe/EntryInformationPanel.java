package intray.gui.entriesframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import intray.model.Entry;

public class EntryInformationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JButton showImageBtn;
	private JButton openFileBtn;
	private JButton undoButton;
	private JButton deleteBtn;
	private JButton doneBtn;
	private JButton openContainingFolderBtn;
	private Entry currentlySelectedEntry;
	private ImageListener imageListener;

	private final int MAX_ROW_WIDTH = 5;
	private int currentlyViewedEntryId = -1;
	private FileListener fileListener;
	private DeleteListener deleteListener;

	public EntryInformationPanel() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 140));
		textArea = new JTextArea(5, 30);
		textArea.setPreferredSize(new Dimension(300, 50));
		showImageBtn = new JButton("Show Image...");
		showImageBtn.setEnabled(false);
		openFileBtn = new JButton("Open File...");
		openFileBtn.setEnabled(false);
		openContainingFolderBtn = new JButton("Open Containing Folder...");
		openContainingFolderBtn.setEnabled(false);
		undoButton = new JButton("Undo");
		undoButton.setEnabled(false);
		deleteBtn = new JButton("Delete Entry");
		deleteBtn.setEnabled(false);
		doneBtn = new JButton("Done");
		doneBtn.setEnabled(true);

		layoutControls();

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		showImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (imageListener != null) {
					imageListener.show();
				}

			}
		});

		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileListener != null) {
					fileListener.openFile();
				}
			}
		});

		openContainingFolderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileListener != null) {
					fileListener.openContainingFolder();
				}
			}
		});

		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (deleteListener != null) {
					deleteListener.deleteCurrentlySelectedEntry();
				}
			}
		});

	}

	private void layoutControls() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		// first row
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = MAX_ROW_WIDTH;
		gc.gridx = 0;
		add(textArea, gc);

		gc.gridwidth = 1;

		// next row
		gc.gridy++;
		gc.weightx = 0.1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		add(deleteBtn, gc);
		gc.gridx++;
		add(showImageBtn, gc);
		gc.gridx++;
		add(openFileBtn, gc);
		gc.gridx++;
		add(openContainingFolderBtn, gc);
		gc.gridx++;
		gc.weightx = 2;
		add(new JLabel(""), gc);

	}

	public void setCurrentEntry(Entry selectedEntry) {
		this.currentlySelectedEntry = selectedEntry;
	}

	public void setTextArea(String title2) {
		this.textArea.setText(title2);
	}

	public void setImageButtonEnabled(boolean hasImage) {
		this.showImageBtn.setEnabled(hasImage);
	}

	public void setOpenFileButtonEnabled(boolean hasFile) {
		this.openFileBtn.setEnabled(hasFile);
	}

	public void setDeleteButtonEnabled(boolean b) {
		this.deleteBtn.setEnabled(b);
	}

	public void setOpenContainingFolderButtonEnabled(boolean b) {
		this.openContainingFolderBtn.setEnabled(b);

	}

	public int getCurrentlyViewedEntryId() {
		return currentlyViewedEntryId;
	}

	public void setCurrentlyViewedEntryId(int selectedEntryId) {
		this.currentlyViewedEntryId = selectedEntryId;
	}

	public void setImageListener(ImageListener listener) {
		this.imageListener = listener;
	}

	public void setFileListener(FileListener fileListener) {
		this.fileListener = fileListener;
	}

	public void setDeleteListener(DeleteListener deleteListener) {
		this.deleteListener = deleteListener;
	}

	public void reset() {
		setCurrentEntry(null);
		setCurrentlyViewedEntryId(-1);
		textArea.setText("");
		showImageBtn.setEnabled(false);
		openFileBtn.setEnabled(false);
		undoButton.setEnabled(false);
		deleteBtn.setEnabled(false);
		doneBtn.setEnabled(false);
		openContainingFolderBtn.setEnabled(false);
	}

}
