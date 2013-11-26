package ror.gui;

import javax.swing.JList;

public class LogList extends JList {
    public LogList() {
	super();
	this.setModel(new LogListModel());
    }
}
