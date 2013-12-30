package ror.gui;

import javax.swing.JTable;

/**
 * @author RoR
 *
 */
public class OrderList extends JTable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor OrderList
	 * 
	 * @param frame
	 */
	public OrderList(RoRFrame frame) {
		super();
		this.setBounds(200, 200, 400, 400);
		this.setModel(new OrderListModel(frame));
	}
}
