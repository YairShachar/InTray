package intray.gui.mainframe;

import java.util.List;

import intray.model.Entry;

public interface IntrayControllerListener {
	public List<Entry> getAllEntries();

	public Entry getEntry(int row);

	public boolean deleteEntry(int row);

	public void deleteMultipleEntries(int[] rows);

	public boolean addEntry(Entry entry);

	public void saveDb();

	public void loadDb();

}
