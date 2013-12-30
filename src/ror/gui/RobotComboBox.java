package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author RoR
 *
 */
@SuppressWarnings("rawtypes")
public class RobotComboBox extends JComboBox implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor RobotComboBox
	 */
	@SuppressWarnings("unchecked")
	public RobotComboBox() {
		super();
		String[] algStore = { "1", "2", "3" };
		DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
		this.setModel(algModel);
		this.setSelectedIndex(2);
		this.addActionListener(this);
		// TODO Faire en reflexif
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComboBox#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().setNbRobot(this.getSelectedIndex() + 1);
		} else {
			System.err.println("Can't get parent RoRFrame");
		}

	}
}
