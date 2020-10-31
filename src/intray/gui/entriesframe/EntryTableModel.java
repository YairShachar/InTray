package intray.gui.entriesframe;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import intray.model.Entry;

public class EntryTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "Entry", "Image", "File" };

	private List<Entry> entries = new ArrayList<Entry>();

	public EntryTableModel() {

	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Entry sectedEntry = entries.get(row);
		switch (column) {
		case 0:
			return sectedEntry.getTitle();
		case 1:
			return sectedEntry.isHasImage();
		case 2:
			return sectedEntry.isHasFile();
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return Boolean.class;
		case 2:
			return Boolean.class;
		}
		return null;
	}

	public void setData(List<Entry> db) {
		this.entries = db;
	}

}
