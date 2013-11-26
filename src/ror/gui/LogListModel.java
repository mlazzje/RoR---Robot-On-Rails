package ror.gui;

import javax.swing.DefaultListModel;

public class LogListModel extends DefaultListModel {
    public LogListModel() {
	super();

	String orders = "Robot #1 prend en charge #111";
	this.addElement(orders);
	this.addElement(orders);
	this.addElement(orders);
	this.addElement(orders);
	this.addElement(orders);
	this.addElement(orders);
	this.addElement(orders);
	this.addElement(orders);
    }
}
