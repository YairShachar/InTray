package intray.gui.mainframe.tests;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TableButtonColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	private ArrayList<TableButtonPressedHandler> handlers;
	private Hashtable<Integer, JPanel> panels;
//	private ArrayList<Jbuttons>
	private Hashtable<String, ImageIcon> iconHashtable;

	public interface TableButtonPressedHandler {
		/**
		 * Called when the button is pressed.
		 * 
		 * @param row    The row in which the button is in the table.
		 * @param column The column the button is in in the table.
		 */
		void onButtonPress(int row, int column);
	}

	public TableButtonColumn() {
		handlers = new ArrayList<TableButtonColumn.TableButtonPressedHandler>();
		panels = new Hashtable<Integer, JPanel>();
		iconHashtable = new Hashtable<String, ImageIcon>();
	}
//
//	public TableButtonColumn(ImageIcon icon) {
//		this.icon = icon;
//		handlers = new ArrayList<TableButtonColumn.TableButtonPressedHandler>();
//		panels = new Hashtable<Integer, JPanel>();
//	}

	public TableButtonColumn(IconPair[] iconPairs) {
		handlers = new ArrayList<TableButtonColumn.TableButtonPressedHandler>();
		panels = new Hashtable<Integer, JPanel>();
		this.iconHashtable = new Hashtable<String, ImageIcon>();
		for (IconPair iconPair : iconPairs) {
			iconHashtable.put(iconPair.getKey(), iconPair.getIcon());
		}
	}

	@Override
	public Object getCellEditorValue() {
		System.out.println("TableButtonColumn: getCellEditorValue: ");
		return null;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
			final int row, final int column) {
		if (value == null) {
			return new JLabel();
		}
		JPanel panel = null;
		if (panels.containsKey(row)) {
			panel = panels.get(row);
		} else {
			panel = new JPanel();
			panel.setLayout(new FlowLayout());
			panel.setAlignmentX(FlowLayout.LEFT);

			if (value != null && value instanceof String[]) {
				for (String key : (String[]) value) {
					JButton btn = new JButton();
					if (iconHashtable.containsKey(key)) {
						btn.setIcon(iconHashtable.get(key));
					} else {
						btn.setText((String) key);
					}
					btn.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (handlers != null) {
								for (TableButtonPressedHandler handler : handlers) {
									handler.onButtonPress(row, column);
								}
							}
						}
					});
					panel.add(btn);
				}

			}

			panels.put(row, panel);

		}

		return panel;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
		JPanel panel = null;
		if (value == null) {
			return new JLabel();
		}
		if (panels.containsKey(row)) {
			panel = panels.get(row);
		} else {
			JButton btn = new JButton();
//			if (icon != null) {
//				btn.setIcon(icon);
//			} else 
			if (value != null && value instanceof String) {
				btn.setText((String) value);
			}
			panel.add(btn);
			panels.put(row, panel);
		}
		return panel;
	}
//
//	public void setButtonText(int row, String text) {
//		JButton button = null;
//		if (panels.containsKey(row)) {
//			button = panels.get(row);
//			button.setText(text);
//		}
//	}

	public void addHandler(TableButtonPressedHandler tableButtonPressedHandler) {
		if (handlers != null) {
			handlers.add(tableButtonPressedHandler);
		}
	}

}
