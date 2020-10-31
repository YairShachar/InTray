package intray.gui.mainframe.tests;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import intray.model.Entry;

public class EntryTableModelEmbedded extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "Entry", "Info", "" };
	private Class[] types = new Class[] { String.class, String[].class, String.class };

	private List<Entry> entries = new ArrayList<Entry>();
	private Object[][] cellData;

	public EntryTableModelEmbedded() {

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
	public Object getValueAt(int row, int col) {

		System.out.println("getValueAt : row: " + row + " col: " + col + " value is: " + cellData[row][col]);
		return cellData[row][col];
	}

	public Class getColumnClass(int column) {
		return types[column];
	}

	public void setData(List<Entry> entries) {
		this.entries = entries;
		createCellData(entries);
	}

	public boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return false;
		}

		return true;
	}

	private void createCellData(List<Entry> entries) {
		cellData = new Object[entries.size()][COLUMN_NAMES.length];
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			for (int j = 0; j < COLUMN_NAMES.length; j++) {
				switch (j) {
				case 0:
//					System.out.println("cellData[" + i + "][" + j + "] = " + entry.getTitle());
					cellData[i][j] = entry.getTitle();
					break;
				case 1:
					ArrayList<String> infoList = new ArrayList<String>();
					cellData[i][j] = new ArrayList<String>();
					if (entry.isHasImage()) {
						infoList.add("image");
					}
					if (entry.isHasFile()) {
						infoList.add("file");
					}
					if (infoList.size() == 0) {
						cellData[i][j] = null;
					} else {
						cellData[i][j] = infoList.toArray(new String[infoList.size()]);
					}
					break;
				case 2:
					infoList = new ArrayList<String>();
					infoList.add("delete");
					cellData[i][j] = infoList.toArray(new String[infoList.size()]);
					break;
				}
			}
		}
	}

	public void refreshCellData() {
		createCellData(this.entries);
	}

}
