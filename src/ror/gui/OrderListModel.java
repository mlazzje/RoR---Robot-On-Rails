package ror.gui;

import java.util.Collections;

import javax.swing.table.DefaultTableModel;

/**
 * OrderListModel class Represent the OrderList Model
 */
public class OrderListModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    private RoRFrame frame;

    /**
     * Constructor of the OrderListModel
     * 
     * @param frame
     *            The RoRFrame
     */
    public OrderListModel(RoRFrame frame) {
	super();
	this.frame = frame;
    }

    @Override
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

    @Override
    public int getRowCount() {
	if (this.frame == null)
	    return 0;
	return this.frame.getUiController().getSimulationManager().getOrders().size();
    }

    @Override
    public int getColumnCount() {
	return 3;
    }

    @Override
    public Object getValueAt(int row, int col) {
	synchronized (this.frame.getUiController().getSimulationManager().getOrders()) {
	    Collections.sort(this.frame.getUiController().getSimulationManager().getOrders());
	    return this.frame.getUiController().getSimulationManager().getOrders().get(row).toWeirdString()[col];
	}

    }

    @Override
    public boolean isCellEditable(int row, int column) {
	return false;
    }
}
