package ror.gui;

import javax.swing.JTable;

/**
 * OrderList class
 * Represent the Order List
 */
public class OrderList extends JTable {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the OrderList class
	 * @param frame The RoRFrame
	 */
	public OrderList(RoRFrame frame) {
		super();
		this.setBounds(200, 200, 400, 400);
		this.setModel(new OrderListModel(frame));
	}
}
