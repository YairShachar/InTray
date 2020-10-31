package intray.gui.mainframe.panel.submitentry;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import intray.gui.mainframe.EntriesFrameListener;
import intray.gui.mainframe.LockListener;
import intray.gui.mainframe.SubmitEntryEventListener;
import intray.gui.mainframe.SuccessAndFailureListener;
import intray.gui.mainframe.TextListener;

public class IntraySubmitEntryPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color DEFAULT_BACKGROUND_COLOR = Color.lightGray;

	public static enum SubmitEntryPanelComponenet {
		TEXT_FIELD, SUBMIT_BUTTON
	};

	private IntrayTextField textField;
	private IntraySubmitEntryButton submitEntryButton;
	private boolean isTextFieldFocusedByUser = true;
	private EntriesFrameListener entriesFrameListener;

	public IntraySubmitEntryPanel(EntriesFrameListener entriesFrameListener) {
		this.entriesFrameListener = entriesFrameListener;
		initTextField();
		initSubmitButton();
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		setBackground(DEFAULT_BACKGROUND_COLOR);

	}

	public String getText() {
		return this.textField.getText();
	}

	public void setText(String string) {
		this.textField.setText(string);

	}

	public JButton getSubmitEntryButton() {
		return this.submitEntryButton;
	}

	public void refresh() {

	}

	public boolean isTextFieldFocused() {
		return this.textField.isFocusOwner();
	}

	public void addMouseListener(SubmitEntryPanelComponenet submitPanelComponent, MouseListener listener) {
		switch (submitPanelComponent) {
		case TEXT_FIELD:
			textField.addMouseListener(listener);
		case SUBMIT_BUTTON:
			submitEntryButton.addMouseListener(listener);

		default:
			break;
		}
	}

	public void setEntryEventListener(SubmitEntryPanelComponenet submitPanelComponent,
			SubmitEntryEventListener listener) {
		switch (submitPanelComponent) {
		case TEXT_FIELD:
			textField.setEntryEventListener(listener);
		case SUBMIT_BUTTON:
			submitEntryButton.setEntryEventListener(listener);
		default:
			break;
		}
	}

	public JTextField getTextField() {
		return this.textField;
	}

	public boolean isTextFieldFocusedByUser() {
		return this.isTextFieldFocusedByUser;
	}

	public Color getDefaultBackgroundColor() {
		return this.DEFAULT_BACKGROUND_COLOR;
	}

	public void setLockListener(LockListener lockListener) {
		this.textField.setLockListener(lockListener);
	}

	public void setFocusOnTextfield() {
		this.textField.requestFocus();

	}

	private void initTextField() {
		this.textField = new IntrayTextField(entriesFrameListener);
		this.textField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				isTextFieldFocusedByUser = true;
			}

			public void mouseClicked(MouseEvent arg0) {
				if (SwingUtilities.isRightMouseButton(arg0)) {
					isTextFieldFocusedByUser = false;
				} else {
					isTextFieldFocusedByUser = true;
				}
			}
		});

		this.textField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				isTextFieldFocusedByUser = false;
			}

			public void focusGained(FocusEvent arg0) {
			}
		});
		this.textField.setSuccessAndFailureListener(new SuccessAndFailureListener() {
			public void onSuccess() {
				submitEntryButton.blinkInColor(IntraySubmitEntryButton.SUCCESS_COLOR);
			}

			public void onFailure() {
				submitEntryButton.blinkInColor(IntraySubmitEntryButton.FAILED_COLOR);
			}
		});
		add(textField);
	}

	private void initSubmitButton() {
		this.submitEntryButton = new IntraySubmitEntryButton();
		submitEntryButton.setTextListener(new TextListener() {
			public void setText(String text) {
				textField.setText(text);
				textField.requestFocusInWindow();
			}

			public String getText() {
				textField.requestFocusInWindow();
				return textField.getText();

			}
		});
		add(submitEntryButton);

	}

}
