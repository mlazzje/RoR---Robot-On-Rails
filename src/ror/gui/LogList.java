package ror.gui;

import javax.swing.JList;

/**
 * @author RoR
 *
 */
@SuppressWarnings("rawtypes")
public class LogList extends JList {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor LogList
	 */
	@SuppressWarnings("unchecked")
	public LogList() {
		super();
		this.setModel(new LogListModel());
	}
}
