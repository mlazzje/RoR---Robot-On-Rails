package ror.gui;

import javax.swing.table.DefaultTableModel;

/**
 * @author RoR
 *
 */
public class OrderListModel extends DefaultTableModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * frame
	 */
	private RoRFrame frame;

	/**
	 * Constructor OrderListModel
	 * 
	 * @param frame
	 */
	public OrderListModel(RoRFrame frame) {
		super();
		this.frame = frame;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "ID";
		case 1:
			return "Produits";
		case 2:
			return "État";
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	public int getRowCount() {
		if (this.frame == null)
			return 0;
		return this.frame.getUiController().getSimulationManager().getOrders().size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		return this.frame.getUiController().getSimulationManager().getOrders().get(row).toWeirdString()[col];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
