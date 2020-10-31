package intray.gui.mainframe.panel.submitentry;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import intray.gui.mainframe.SubmitEntryEventListener;
import intray.gui.mainframe.TextListener;

public class IntraySubmitEntryButton extends JButton {
	public static final Color SUCCESS_COLOR = new Color(10, 100, 255);
	public static final Color FAILED_COLOR = Color.red;
	private static final Color DEFAULT_COLOR = Color.gray;
	private static final String ICON_FILE_PATH = "/intray_icon.png";
	private static final long serialVersionUID = 1L;
	protected static final long BLINK_TIME = 150;
	private TextListener textListener;
	private SubmitEntryEventListener entryEventListener;

	public IntraySubmitEntryButton() {
		super("");
		setSize(9, 94);
		setBackground(DEFAULT_COLOR);
		setIcon();
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EntryEvent event = new EntryEvent(EntryEventType.SUBMIT_TEXT, textListener.getText());
				if (entryEventListener.submit(event)) {
					textListener.setText("");
					blinkInColor(SUCCESS_COLOR);
				} else {
					blinkInColor(FAILED_COLOR);
				}
			}

		});

	}

	public void setTextListener(TextListener listener) {
		this.textListener = listener;
	}

	public void setEntryEventListener(SubmitEntryEventListener listener) {
		this.entryEventListener = listener;

	}

	public void blinkInColor(Color color) {
		setBackground(color);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(BLINK_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setBackground(DEFAULT_COLOR);
			}
		});

	}

	private void setIcon() {
		URL iconUrl = getClass().getResource(ICON_FILE_PATH);
		ImageIcon icon = new ImageIcon(iconUrl);
		this.setIcon(icon);
	}

}
