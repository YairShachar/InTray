package intray.gui.dialog.preferences;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import intray.gui.entriesframe.PrefsListener;
import intray.gui.mainframe.IntrayMainFrame;
import intray.logic.IntrayConstants;

public class IntrayPrefsDialog extends JDialog {

	private JTextField workingDirTextField;
	private JButton okBtn;
	private JButton cancelBtn;
	private JButton browseBtn;
	private PrefsListener prefsListener;
	private JFileChooser fileChooser;

	public IntrayPrefsDialog(IntrayMainFrame parent) {
		super(parent, "Preferences", true);
		setLayout(new GridBagLayout());
		setLocationRelativeTo(parent);
		setSize(600, 150);
		setMinimumSize(new Dimension(570,150));
		setMaximumSize(new Dimension(610,160));
		workingDirTextField = new JTextField(30);
		workingDirTextField.setEditable(false);
		okBtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");
		browseBtn = new JButton("Browse...");
		fileChooser = new JFileChooser();

		setLayoutControls();

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = workingDirTextField.getText();
				File file = new File(text);
				if (!Files.exists(Paths.get(file.getAbsolutePath()).resolve(IntrayConstants.DATABASE_FILE_NAME))) {
					int response = JOptionPane.showConfirmDialog(IntrayPrefsDialog.this,
							"This directory does not contain an intray database, would you like to initialize one here?",
							"Intray Database Initialization", JOptionPane.YES_NO_OPTION);
					if (response == JOptionPane.NO_OPTION) {
						return;
					}
				}
				try {
					String workingDir = file.getAbsolutePath();
					Files.createDirectories(Paths.get(workingDir));
					prefsListener.preferencesSet(workingDir);
					setVisible(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		browseBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setCurrentDirectory(new File(workingDirTextField.getText()));
				if (fileChooser.showOpenDialog(IntrayPrefsDialog.this) == JFileChooser.APPROVE_OPTION) {
					File dir = fileChooser.getSelectedFile();
					workingDirTextField.setText(dir.getAbsolutePath());
				}
			}
		});
	}

	private void setLayoutControls() {
		GridBagConstraints gc = new GridBagConstraints();

		/*
		 * TODO
		 */
		// JPanel perferencesPanel = new JPanel();
		// JPanel buttonsPanel = new JPanel();

		// first row
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;

		gc.gridx = 0;
		add(new JLabel("Intray directory: "), gc);
		gc.gridx++;
		add(workingDirTextField, gc);
		gc.gridx++;
		add(browseBtn, gc);

		// next row

		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.NONE;

		gc.anchor = GridBagConstraints.EAST;
		gc.gridx = 0;
		add(okBtn, gc);

		gc.anchor = GridBagConstraints.WEST;
		gc.gridx++;
		add(cancelBtn, gc);
	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.prefsListener = prefsListener;
	}

	public void setDefaults(String workingDir) {
		workingDirTextField.setText(workingDir);
		prefsListener.preferencesSet(workingDir);
	}

}
