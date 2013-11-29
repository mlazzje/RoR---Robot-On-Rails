package ror.gui;

import java.awt.Color;

import javax.swing.JTable;

public class OrderList extends JTable {
    public OrderList(RoRFrame frame) {
	super();
	this.setBounds(200, 200, 400, 400);
	this.setModel(new OrderListModel(frame));
    }
}
