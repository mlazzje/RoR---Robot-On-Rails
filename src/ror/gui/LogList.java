package ror.gui;

import javax.swing.JList;

@SuppressWarnings("rawtypes")
/**
 * LogList class
 * Represent the LogList
 */
public class LogList extends JList {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the LogList class
	 */
	@SuppressWarnings("unchecked")
	public LogList() {
		super();
		this.setModel(new LogListModel());
	}
}
