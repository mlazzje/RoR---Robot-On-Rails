package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

@SuppressWarnings("rawtypes")
/**
 * RobotComboBox class
 * Represent the Robot ComboBox
 */
public class RobotComboBox extends JComboBox implements ActionListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the RobotComboBox
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

	/**
	 * Function called when item selected
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
