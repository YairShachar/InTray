package intray.gui.entriesframe;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import intray.model.Entry;

public class EntryTablePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntryTable entryTable;
	private EntryTableModel entryTableModel;
	private TableSelectionListener entryTableSelectionListener;
	private ImageListener imageListener;
	private FileListener fileListener;

	private DeleteListener deleteListener;

	private static final int CHECKBOX_COLUMN_WIDTH = 50;

	public EntryTablePanel(int width) {
		setLayout(new BorderLayout());
		entryTableModel = new EntryTableModel();
		entryTable = new EntryTable(entryTableModel);
		entryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		entryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = entryTable.getSelectedRow();
				if (entryTableSelectionListener != null) {
					entryTableSelectionListener.onEntrySelected(row);
				}
			}
		});

		entryTable.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() == 2) {
					showSelectedEntryMedia();
				}
			}
		});

		entryTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_DELETE) && ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0)) {
					deleteListener.deleteCurrentlySelectedEntryNoConfirm();
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteListener.deleteCurrentlySelectedEntry();
				}
			}
		});

		int colNum = -1;
		// setting column dimensions
		// column 0
		colNum++;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		entryTable.getColumnModel().getColumn(colNum).setPreferredWidth(screenSize.width);
		entryTable.getColumnModel().getColumn(colNum).setMinWidth(100);

		// next column
		colNum++;
		entryTable.getColumnModel().getColumn(colNum).setPreferredWidth(CHECKBOX_COLUMN_WIDTH);
		entryTable.getColumnModel().getColumn(colNum).setMinWidth(CHECKBOX_COLUMN_WIDTH);
		entryTable.getColumnModel().getColumn(colNum).setResizable(false);
		// next column
		colNum++;
		entryTable.getColumnModel().getColumn(colNum).setPreferredWidth(CHECKBOX_COLUMN_WIDTH);
		entryTable.getColumnModel().getColumn(colNum).setMinWidth(CHECKBOX_COLUMN_WIDTH);
		entryTable.getColumnModel().getColumn(colNum).setResizable(false);

		//
		entryTable.setRowHeight(37);
		//

		entryTable.setPreferredScrollableViewportSize(new Dimension(700, 700));
		JScrollPane scrollPane = new JScrollPane(entryTable);
		add(scrollPane, BorderLayout.CENTER);
	}

	protected void showSelectedEntryMedia() {
		if (imageListener.show()) {
			return;
		}
		if (fileListener.openFile()) {
			return;
		}
	}

	class JTableButtonRenderer implements TableCellRenderer {
		private TableCellRenderer defaultRenderer;

		public JTableButtonRenderer(TableCellRenderer renderer) {
			defaultRenderer = renderer;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value instanceof Component)
				return (Component) value;
			return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}

	public void setData(List<Entry> db) {
		entryTableModel.setData(db);
	}

	public void refresh() {
		entryTableModel.fireTableDataChanged();
		entryTableSelectionListener.onEntrySelected(getSelectedEntryId());

	}

	public boolean isEntryTableFocused() {
		return this.entryTable.isFocusOwner();
	}

	public void setTableSelectiontListener(TableSelectionListener listener) {
		this.entryTableSelectionListener = listener;
	}

	public int getSelectedEntryId() {
		return this.entryTable.getSelectedRow();
	}

	public void setImageListener(ImageListener imageListener) {
		this.imageListener = imageListener;
	}

	public void setFileListener(FileListener fileListener) {
		this.fileListener = fileListener;
	}

	public void setDeleteListener(DeleteListener deleteListener) {
		this.deleteListener = deleteListener;
	}

	public void selectEntry(int i) {
		if (entryTable.getRowCount() > 0) {
			this.entryTable.setRowSelectionInterval(i, i);
			this.entryTableSelectionListener.onEntrySelected(i);
		}

	}

}
