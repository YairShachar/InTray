package intray.gui.listeners.defaults;

import java.io.IOException;
import java.util.List;

import intray.gui.mainframe.IntrayControllerListener;
import intray.gui.mainframe.IntrayMainFrame;
import intray.model.Entry;

public class DefaultIntrayControllerListener implements IntrayControllerListener {

	private IntrayMainFrame intrayMainFrame;

	public DefaultIntrayControllerListener(IntrayMainFrame intrayMainFrame) {
		this.intrayMainFrame = intrayMainFrame;
	}

	public List<Entry> getAllEntries() {
		return intrayMainFrame.getController().getEntries();
	}

	public boolean deleteEntry(int i) {
		return intrayMainFrame.getController().deleteEntry(i);
	}

	public void deleteMultipleEntries(int[] indexes) {
		intrayMainFrame.getController().deleteMultipleEntries(indexes);
	}

	public boolean addEntry(Entry entryData) {
		if (intrayMainFrame.getController().addEntry(entryData)) {
			if (intrayMainFrame.getCurrentEntriesFrame() != null) {
				intrayMainFrame.getCurrentEntriesFrame().refresh();
			}
			return true;
		}

		return false;

	}

	@Override
	public void saveDb() {
		try {
			intrayMainFrame.getController().saveDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadDb() {
		try {
			intrayMainFrame.getController().loadDataBase(intrayMainFrame.getWorkingDir());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Entry getEntry(int row) {
		return intrayMainFrame.getController().getEntry(row);
	}


}
