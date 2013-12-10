package ror.gui;

import javax.swing.JList;

@SuppressWarnings("rawtypes")
public class LogList extends JList {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public LogList() {
		super();
		this.setModel(new LogListModel());
	}
}
