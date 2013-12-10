package ror.gui;

import javax.swing.table.DefaultTableModel;

public class OrderListModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private RoRFrame frame;

	public OrderListModel(RoRFrame frame) {
		super();
		this.frame = frame;
	}

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

	public int getRowCount() {
		if (this.frame == null)
			return 0;
		return this.frame.getUiController().getSimulationManager().getOrders().size();
	}

	public int getColumnCount() {
		return 3;
	}

	public Object getValueAt(int row, int col) {
		return this.frame.getUiController().getSimulationManager().getOrders().get(row).toWeirdString()[col];
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
