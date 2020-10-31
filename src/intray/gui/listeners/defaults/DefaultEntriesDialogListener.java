package intray.gui.listeners.defaults;

import intray.gui.entriesframe.EntryListFrame;
import intray.gui.mainframe.EntriesFrameListener;
import intray.gui.mainframe.IntrayMainFrame;

public class DefaultEntriesDialogListener implements EntriesFrameListener {

	private IntrayMainFrame intrayMainFrame;

	public DefaultEntriesDialogListener(IntrayMainFrame intrayMainFrame) {
		this.intrayMainFrame = intrayMainFrame;
	}

	@Override
	public void openWindow() {
		EntryListFrame ef = this.intrayMainFrame.getCurrentEntriesFrame();
		if (ef == null) {
			intrayMainFrame.openNewEntriesFrame();
		} else {
			ef.setVisible(true);
		}
	}

	@Override
	public void showSelectedImage() {

	}

}
