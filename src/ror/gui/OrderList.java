package ror.gui;

import javax.swing.JTable;

public class OrderList extends JTable {

	private static final long serialVersionUID = 1L;

	public OrderList(RoRFrame frame) {
		super();
		this.setBounds(200, 200, 400, 400);
		this.setModel(new OrderListModel(frame));
	}
}
