package intray.gui.mainframe.panel.submitentry;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import intray.gui.mainframe.EntriesFrameListener;
import intray.gui.mainframe.LockListener;
import intray.gui.mainframe.SubmitEntryEventListener;
import intray.gui.mainframe.SuccessAndFailureListener;

public class IntrayTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	private static final int TEXT_WIDTH = 140;
	private static final int TEXT_HEIGHT = 27;
	private SubmitEntryEventListener entryEventListener;
	private SuccessAndFailureListener successAndFailureListener;
	private LockListener lockListener;
	private EntriesFrameListener entriesFrameListener;

	
	public IntrayTextField(EntriesFrameListener entriesFrameListener) {
		super("");
		this.entriesFrameListener = entriesFrameListener;
		setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));
		setMaximumSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));
		initDropTargetFunctionality();
		initKeyListeners();
		Font font = new Font("SansSerif", Font.PLAIN, 11);
		setFont(font);
	}

	private void initKeyListeners() {

		addKeyListener(new KeyListener() {
			

			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				try {
					handleCtrlV(e);
					handleCtrlO(e);
					handleCtrlL(e);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}

			private void handleCtrlL(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					lockListener.lock();
				}
			}

			private void handleCtrlV(KeyEvent e) throws HeadlessException, IOException {
				if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					try {
						BufferedImage image = (BufferedImage) Toolkit.getDefaultToolkit().getSystemClipboard()
								.getData(DataFlavor.imageFlavor);
						submitImage(image, null);

					} catch (UnsupportedFlavorException e1) {
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						Transferable tran = clipboard.getContents(null);
						try {
							if (tran != null && tran.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
								@SuppressWarnings("unchecked")
								List<File> list = (List<File>) tran.getTransferData(DataFlavor.javaFileListFlavor);
								for (File file : list) {
									submitFile(file);
								}
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}

					}
				}
			}

			private void handleCtrlO(KeyEvent e) throws HeadlessException, IOException {
				if ((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					entriesFrameListener.openWindow();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	private void initDropTargetFunctionality() {
		setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						submitFile(file);

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private void submitFile(File file) {
		String title = file.getName();// askForTitle();
		EntryEvent event = new EntryEvent(EntryEventType.SUBMIT_FILE, title);
		event.setFilePath(file.getAbsolutePath());
		if (entryEventListener.submit(event)) {
			successAndFailureListener.onSuccess();
			setText("");
		} else {
			successAndFailureListener.onFailure();
		}

	}

	private void submitImage(BufferedImage image, String format) {
		String title = askForTitle();
		EntryEvent event = new EntryEvent(EntryEventType.SUBMIT_IMAGE, title);
		event.setImage(image);
		event.setFormat(format);
		if (entryEventListener.submit(event)) {
			successAndFailureListener.onSuccess();
			setText("");
		} else {
			successAndFailureListener.onFailure();
		}

	}

	private String askForTitle() {
		return JOptionPane.showInputDialog("Please enter a title", this.getText());
	}

	public void setEntryEventListener(SubmitEntryEventListener listener) {
		this.entryEventListener = listener;
	}

	public void setSuccessAndFailureListener(SuccessAndFailureListener listener) {
		this.successAndFailureListener = listener;
	}
	public void setLockListener(LockListener lockListener) {
		this.lockListener = lockListener;
	}




}
