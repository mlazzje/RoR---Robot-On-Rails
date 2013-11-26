package ror.gui;

import javax.swing.table.DefaultTableModel;

public class OrderListModel extends DefaultTableModel {
    public OrderListModel() {
	super();
	this.addColumn("#");
	this.addColumn("Articles");
	this.addColumn("Etat");

	String[] orders = { "#1123", "1, 2, 3, 4", "En attente" };
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
	this.addRow(orders);
    }

    public boolean isCellEditable(int row, int column) {
	return false;
    }
}
